package com.xusy.springbt.util;

import com.xusy.springbt.annotation.ExcelColumn;
import com.xusy.springbt.exception.ApplicationException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//Excel工具类
@Service
public class ExcelService {
    private static Logger log = LoggerFactory.getLogger(ExcelService.class);

    private final static String EXCEL2003 = "xls";
    private final static String EXCEL2007 = "xlsx";

    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    private Semaphore semaphore = new Semaphore(20);

    @SneakyThrows
    public <T> Map<String, Object> readExcelFile(Class<T> cls, MultipartFile file) {
        return readExcelInputStream(cls, file.getInputStream(), file.getOriginalFilename());
    }

    public <T> Map<String, Object> readExcelInputStream(Class<T> cls, InputStream is, String fileName) {
        Map<String, Object> data = new HashMap<>();
        data.put("fileName", fileName);
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new RuntimeException("上传文件格式不正确");
        }
        List<T> dataList = new CopyOnWriteArrayList<>();
        List<Map<String, Object>> dataMapList = new CopyOnWriteArrayList<>();
        Workbook workbook = null;
        try {
            if (fileName.endsWith(EXCEL2007)) {
                workbook = new XSSFWorkbook(is);
            }
            if (fileName.endsWith(EXCEL2003)) {
                workbook = new HSSFWorkbook(is);
            }
            if (workbook != null) {
                //类映射  注解 value-->bean property
                Map<String, Field> classMap = new HashMap<>(17);
                List<Field> fields = Stream.of(cls.getDeclaredFields()).collect(Collectors.toList());
                fields.forEach(
                        field -> {
                            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                            if (annotation != null) {
                                String value = annotation.value();
                                if (StringUtils.isBlank(value)) {
                                    return;//return起到的作用和continue是相同的 语法
                                }
                                if (!classMap.containsKey(value)) {
                                    field.setAccessible(true);
                                    classMap.put(value, field);
                                }
                            }
                        }
                );
                //索引-->columns
                Map<Integer, Field> reflectionMap = new HashMap<>(17);
                //默认读取第一个sheet
                Sheet sheet = workbook.getSheetAt(0);
                //首行 表头
                Row row = sheet.getRow(0);
                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    String cellValue = getCellValue(cell);
                    if (classMap.containsKey(cellValue)) {
                        reflectionMap.put(j, classMap.get(cellValue));
                    }
                }
                if (classMap.size() != reflectionMap.size()) {
                    throw new ApplicationException("Excel列数不正确，请修改");
                }
                //多线程解析数据
                List<List<Row>> rowlists = groupList(sheet, 5000);
                final CountDownLatch latch = new CountDownLatch(rowlists.size());
                for (List<Row> rowlist : rowlists) {
                    int rowlistSize = rowlist.size();
                    System.out.println("rowlist.size()==" + rowlistSize);
                    semaphore.acquire();
                    fixedThreadPool.execute(() -> {
                        try {
//                            List<Map<String,Object>> theDataList = new ArrayList<>(rowlistSize);
                            rowlist.forEach(r -> {
                                if (r == null) {
                                    return;
                                }
                                int rowNum = r.getRowNum();
                                try {
                                    T t = cls.newInstance();
                                    //判断是否为空白行
                                    boolean allBlank = true;
                                    for (int j = r.getFirstCellNum(); j <= r.getLastCellNum(); j++) {
                                        if (reflectionMap.containsKey(j)) {
                                            Cell cell = r.getCell(j);
                                            String cellValue = getCellValue(cell);
                                            if (StringUtils.isNotBlank(cellValue)) {
                                                allBlank = false;
                                            }
                                            Field field = reflectionMap.get(j);
                                            try {
                                                handleField(t, cellValue, field);
                                            } catch (Exception e) {
                                                throw new ApplicationException(String.format("reflect field:%s value:%s exception!", field.getName(), cellValue), e);
                                            }
                                        }
                                    }
                                    if (!allBlank) {
                                        dataList.add(t);
                                        dataMapList.add(MapUtils.object2Map(t));
                                    } else {
                                        log.warn(String.format("row:%s is blank ignore!", rowNum));
                                    }
                                } catch (Exception e) {
                                    throw new ApplicationException(String.format("parse row:%s exception!", rowNum), e);
                                }
                            });
                        } catch (Exception e) {
                            log.error("", e);
                        } finally {
                            semaphore.release();
                            latch.countDown();
                        }
                    });
                }
                //关闭线程池
                fixedThreadPool.shutdown();
                latch.await();
            }
        } catch (Exception e) {
            throw new ApplicationException(String.format("parse excel exception!%s", e));
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    throw new ApplicationException(String.format("parse excel exception!%s", e));
                }
            }
        }
        data.put("list", dataList);
        data.put("listMap", dataMapList);
        return data;
    }

    private static <T> void handleField(T t, String value, Field field) throws Exception {
        Class<?> type = field.getType();
        if (type == null || type == void.class || StringUtils.isBlank(value)) {
            return;
        }
        if (type == Object.class) {
            field.set(t, value);
            //数字类型
        } else if (type.getSuperclass() == null || type.getSuperclass() == Number.class) {
            if (type == int.class || type == Integer.class) {
                field.set(t, NumberUtils.toInt(value.substring(0, value.lastIndexOf("."))));
            } else if (type == long.class || type == Long.class) {
                field.set(t, NumberUtils.toLong(value));
            } else if (type == byte.class || type == Byte.class) {
                field.set(t, NumberUtils.toByte(value));
            } else if (type == short.class || type == Short.class) {
                field.set(t, NumberUtils.toShort(value));
            } else if (type == double.class || type == Double.class) {
                field.set(t, NumberUtils.toDouble(value));
            } else if (type == float.class || type == Float.class) {
                field.set(t, NumberUtils.toFloat(value));
            } else if (type == char.class || type == Character.class) {
                field.set(t, CharUtils.toChar(value));
            } else if (type == boolean.class) {
                field.set(t, BooleanUtils.toBoolean(value));
            } else if (type == BigDecimal.class) {
                if (isRealEmpty(value)) {
                    field.set(t, null);
                } else {
                    field.set(t, new BigDecimal(value));
                }
            }
        } else if (type == Boolean.class) {
            field.set(t, BooleanUtils.toBoolean(value));
        } else if (type == Date.class) {
            //
            field.set(t, value);
        } else if (type == String.class) {
            field.set(t, value);
        } else {
            Constructor<?> constructor = type.getConstructor(String.class);
            field.set(t, constructor.newInstance(value));
        }
    }

    private static boolean isRealEmpty(String value) {
        return StringUtils.isEmpty(value) || "null".equalsIgnoreCase(value) ? true : false;
    }

    //分组组装list
    private List<List<Row>> groupList(Sheet sheet, int capacitySize) {
        int firstRowNum = sheet.getFirstRowNum() + 1;
        int lastRowNum = sheet.getLastRowNum();
        List<Row> totalList = new ArrayList<>(lastRowNum);
        for (int i = firstRowNum; i <= lastRowNum; i++) {
            totalList.add(sheet.getRow(i));
        }
        int length = totalList.size();
        List<List<Row>> rowListList = new ArrayList<>();
        int groupSize = (length + capacitySize - 1) / capacitySize;
        for (int i = 0; i < groupSize; i++) {
            List<Row> rowList = new ArrayList<>(capacitySize);
            // 开始位置
            int fromIndex = i * capacitySize;
            // 结束位置
            int toIndex = (i + 1) * capacitySize < length ? (i + 1) * capacitySize : length;
            rowList.addAll(totalList.subList(fromIndex, toIndex));
            rowListList.add(rowList);
        }
        return rowListList;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
            } else {
                String numericValue = String.valueOf(cell.getNumericCellValue());
                if (numericValue.endsWith(".0")) {
                    return numericValue.replace(".0", "");
                } else if (numericValue.contains("E") && numericValue.contains(".")) {
                    //科学计数法格式
                    DecimalFormat df = new DecimalFormat("0");
                    numericValue = df.format(cell.getNumericCellValue());
                }
                return numericValue;
            }
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return StringUtils.trimToEmpty(cell.getStringCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return new BigDecimal(cell.getNumericCellValue()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return "";
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
            return "ERROR";
        } else {
            return cell.toString().trim();
        }
    }
}
