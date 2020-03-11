package com.xusy.springbt.util.poi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class ExcelEventParser {

    enum xssfDataType {
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER,
    }

    class XLSReader implements HSSFListener {

        //POIFS文件流
        private POIFSFileSystem fs;
        //当前行数
        private int lastRowNumber;
        //当前列数
        private int lastColumnNumber;
        //是否计算公式的值
        private boolean outputFormulaValues = true;

        private SheetRecordCollectingListener workbookBuildingListener;

        private HSSFWorkbook stubWorkbook;
        //共享字符集
        private SSTRecord sstRecord;

        private FormatTrackingHSSFListener formatListener;

        private int sheetIndex = -1;

        private List<Record> boundSheetRecords = new ArrayList<Record>();
        //公式单元格结果所在行
        private int nextRow;
        //公式单元格结果所在列
        private int nextColumn;
        //公式单元格是否存在计算值
        private boolean outputNextStringRecord;
        //当前行数
        private int curRow = 0;
        //当前最大列数
        private int maxRef = 0;

        //空单元格使用的填充字符
        private String NULL = "-";

        private List<String> row = new ArrayList<String>();

        private List<List<String>> rows = new ArrayList<List<String>>();

        /**
         * 读取Excel2003文件
         *
         * @param filePath 文件路径
         * @return
         * @throws IOException
         */
        public List<List<String>> process(String filePath) throws IOException {
            //使用POIFS读取文件流
            this.fs = new POIFSFileSystem(new FileInputStream(filePath));
            MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
            formatListener = new FormatTrackingHSSFListener(listener);
            HSSFEventFactory factory = new HSSFEventFactory();
            HSSFRequest request = new HSSFRequest();

            if (outputFormulaValues) {
                //添加全部监听器
                request.addListenerForAllRecords(formatListener);
            } else {
                workbookBuildingListener = new SheetRecordCollectingListener(formatListener);
                request.addListenerForAllRecords(workbookBuildingListener);
            }
            //处理文件流
            factory.processWorkbookEvents(request, fs);
            return rows;
        }

        /**
         * 依次处理每个监听的record
         *
         * @param record
         * @return
         * @throws IOException
         */
        public void processRecord(Record record) {
            //只处理第一个sheet，其他全部忽略
            if (sheetIndex > 0) {
                return;
            }
            if (maxRef > MAX_COLUMN) {
                return;
            }
            int thisRow = -1;
            int thisColumn = -1;
            String thisStr = null;
            String value = null;
            //根据record的sid匹配类型分别进行处理
            //可以在这里添加业务逻辑
            switch (record.getSid()) {
                // 记录了sheetName
                case BoundSheetRecord.sid:
                    boundSheetRecords.add(record);
                    break;
                // Workbook、Sheet的开始
                case BOFRecord.sid:
                    BOFRecord br = (BOFRecord) record;
                    if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
                        if (workbookBuildingListener != null && stubWorkbook == null) {
                            stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
                        }
                        sheetIndex++;
                    }
                    break;
                //共享字符集
                case SSTRecord.sid:
                    sstRecord = (SSTRecord) record;
                    break;
                // 存在单元格样式的空单元格
                case BlankRecord.sid:
                    BlankRecord brec = (BlankRecord) record;
                    thisRow = brec.getRow();
                    thisColumn = brec.getColumn();
                    thisStr = NULL;
                    row.add(thisColumn, thisStr);
                    break;
                // 布尔或错误单元格
                case BoolErrRecord.sid:
                    BoolErrRecord berec = (BoolErrRecord) record;
                    thisRow = berec.getRow();
                    thisColumn = berec.getColumn();
                    thisStr = berec.getBooleanValue() + "";
                    row.add(thisColumn, thisStr);
                    break;
                // 公式单元格
                case FormulaRecord.sid:
                    //调用方法计算单元格的值
                    FormulaRecord frec = (FormulaRecord) record;
                    thisRow = frec.getRow();
                    thisColumn = frec.getColumn();
                    if (outputFormulaValues) {
                        if (Double.isNaN(frec.getValue())) {
                            outputNextStringRecord = true;
                            nextRow = frec.getRow();
                            nextColumn = frec.getColumn();
                        } else {
                            thisStr = formatListener.formatNumberDateCell(frec);
                        }
                    } else {
                        thisStr = '"' + HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression()) + '"';
                    }
                    row.add(thisColumn, thisStr);
                    break;
                // 公式的计算结果单元格
                case StringRecord.sid:
                    if (outputNextStringRecord) {
                        StringRecord srec = (StringRecord) record;
                        thisStr = srec.getString();
                        thisRow = nextRow;
                        thisColumn = nextColumn;
                        outputNextStringRecord = false;
                    }
                    break;
                // 文本单元格
                case LabelRecord.sid:
                    LabelRecord lrec = (LabelRecord) record;
                    curRow = thisRow = lrec.getRow();
                    thisColumn = lrec.getColumn();
                    value = lrec.getValue().trim();
                    value = value.equals("") ? NULL : value;
                    this.row.add(thisColumn, value);
                    break;
                // 共用的文本单元格
                case LabelSSTRecord.sid:
                    LabelSSTRecord lsrec = (LabelSSTRecord) record;
                    curRow = thisRow = lsrec.getRow();
                    thisColumn = lsrec.getColumn();
                    if (sstRecord == null) {
                        row.add(thisColumn, NULL);
                    } else {
                        value = sstRecord.getString(lsrec.getSSTIndex()).toString().trim();
                        value = value.equals("") ? NULL : value;
                        row.add(thisColumn, value);
                    }
                    break;
                // 数值单元格：数字单元格和日期单元格
                case NumberRecord.sid:
                    NumberRecord numrec = (NumberRecord) record;
                    curRow = thisRow = numrec.getRow();
                    thisColumn = numrec.getColumn();
                    //处理时间类型的数字
                    value = formatListener.formatNumberDateCell(numrec).trim();
                    value = value.equals("") ? NULL : value;
                    row.add(thisColumn, value);
                    break;

                default:
                    break;
            }

            if (thisRow != -1 && thisRow != lastRowNumber) {
                lastColumnNumber = -1;
            }

            if (record instanceof MissingCellDummyRecord) {
                MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
                curRow = thisRow = mc.getRow();
                thisColumn = mc.getColumn();
                row.add(thisColumn, NULL);
            }

            if (thisRow > -1)
                lastRowNumber = thisRow;
            if (thisColumn > -1)
                lastColumnNumber = thisColumn;
            //进入行结束处理
            if (record instanceof LastCellOfRowDummyRecord) {

                if (maxRef > row.size()) {
                    int len = maxRef - row.size();
                    for (int i = 0; i < len; i++) {
                        lastColumnNumber++;
                        row.add(lastColumnNumber, NULL);
                    }
                }
                if (curRow == 0) {
                    maxRef = row.size();
                }
                rows.add(row);
                lastColumnNumber = -1;
                row = new ArrayList<String>();
            }
        }
    }

    class XLSXReader extends DefaultHandler {

        //样式数据
        private StylesTable stylesTable;
        //共享字符集
        private ReadOnlySharedStringsTable sharedStringsTable;
        //是否存在内联字符
        private boolean vIsOpen;
        //记录数据类型
        private xssfDataType nextDataType;

        private short formatIndex;
        private String formatString;
        //格式化类
        private final DataFormatter formatter;
        //当前所在列
        private int thisColumn = -1;
        //当前单元格值
        private StringBuffer value;
        //储存一列的数据
        private List<String> record = new ArrayList<String>();
        //储存全部数据
        private List<List<String>> rows = new ArrayList<List<String>>();
        //判断当前行有没有空单元格
        private boolean isCellNull = false;
        //前一个单元格和当前单元格
        private String preRef = null, ref = null;
        //避免同一个单元格重复计数
        private String Newcell = null;
        //计数当前行中的空格
        private int rowNull = 0;
        //当前单元格对应的序号
        private int curCol = 0;
        //当前行对应的序号
        private int curRow = 0;
        //空单元格使用的填充字符
        private String NULL = "-";

        /**
         * XLSXReader构造函数
         *
         * @param styles  单元格样式数据
         * @param strings 共享字符集
         */
        public XLSXReader(StylesTable styles, ReadOnlySharedStringsTable strings) {
            this.stylesTable = styles;
            this.sharedStringsTable = strings;
            this.value = new StringBuffer();
            this.nextDataType = xssfDataType.NUMBER;
            this.formatter = new DataFormatter();
            record = new ArrayList<String>();
            rows.clear();
        }

        /**
         * 每当遇到开始标签时进入
         *
         * @param uri        命名空间路径或空
         * @param localName  本地名称或空
         * @param name       标签名称
         * @param attributes 标签内容
         * @return
         * @throws SAXException
         */
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            if ("inlineStr".equals(name) || "v".equals(name)) {
                vIsOpen = true;
                value.setLength(0);
                //每当遇到<c>表示新的单元格
            } else if ("c".equals(name)) {
                //记录上一个单元格和当前单元格用于填充空格
                if (preRef == null) {
                    preRef = attributes.getValue("r");
                } else {
                    preRef = ref;
                }
                ref = attributes.getValue("r");

                String r = ref;
                int firstDigit = -1;
                for (int c = 0; c < r.length(); ++c) {
                    if (Character.isDigit(r.charAt(c))) {
                        firstDigit = c;
                        break;
                    }
                }
                //获取当前列数
                thisColumn = nameToColumn(r.substring(0, firstDigit));
                //判断是否超过最大列数
                if (thisColumn > MAX_COLUMN) {
                    return;
                }
                //将单元格类型翻译成设定名称
                this.nextDataType = xssfDataType.NUMBER;
                this.formatIndex = -1;
                this.formatString = null;
                String cellTpye = attributes.getValue("t");
//                if (StringUtils.isEmpty(cellTpye)) {
//                    record.add(thisColumn, null);
//                    curCol++;
//                }
                String cellStyleStr = attributes.getValue("s");
                if ("b".equals(cellTpye))
                    nextDataType = xssfDataType.BOOL;
                else if ("e".equals(cellTpye))
                    nextDataType = xssfDataType.ERROR;
                else if ("inlineStr".equals(cellTpye))
                    nextDataType = xssfDataType.INLINESTR;
                else if ("s".equals(cellTpye))
                    nextDataType = xssfDataType.SSTINDEX;
                else if ("str".equals(cellTpye))
                    nextDataType = xssfDataType.FORMULA;
                else if (cellStyleStr != null) {
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                    this.formatIndex = style.getDataFormat();
                    this.formatString = style.getDataFormatString();

                    if (this.formatString == null) {
                        this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
                    }
                }
            }
        }

        /**
         * 每当遇到结束标签时进入
         *
         * @param uri       命名空间路径或空
         * @param localName 本地名称或空
         * @param name      标签名称
         * @return
         * @throws SAXException
         */
        public void endElement(String uri, String localName, String name) throws SAXException {
            //当遇到</v>标签时，表面存在一个需要处理的值
            if ("v".equals(name)) {
                if (thisColumn > MAX_COLUMN) {
                    return;
                }
                String thisStr = NULL;
                //根据当前单元格和上一个单元格判断需要补充的空单元格
                if (null != ref && !ref.equals(Newcell)) {
                    if (!ref.equals(preRef)) {
                        int num = Integer.parseInt(ref.replaceAll("[A-Z]", ""));
                        int num_1 = Integer.parseInt(preRef.replaceAll("[A-Z]", ""));
                        int len = 0;
                        //取出两个单元格所在行数，如果行数不同则进行换行，从A1开始填充
                        if (num > num_1) {
                            len = countNullCell(ref, "A1") + 1;
                        } else {
                            len = countNullCell(ref, preRef);
                        }
                        //填充空单元格
                        for (int i = 0; i < len; i++) {
                            record.add(curCol, thisStr);
                            curCol++;
                            Newcell = ref;
                            rowNull++;
                        }
                    }
                }
                //选择合适的方式处理单元格
                switch (nextDataType) {

                    case BOOL:
                        char first = value.charAt(0);
                        thisStr = first == '0' ? "否" : "是";
                        break;

                    case ERROR:
                        thisStr = value.toString();
                        break;

                    case FORMULA:
                        thisStr = value.toString();
                        break;

                    case INLINESTR:
                        XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                        thisStr = rtsi.toString();
                        break;

                    case SSTINDEX:
                        String sstIndex = value.toString();
                        try {
                            //查询字符索引对应的字符值
                            int idx = Integer.parseInt(sstIndex);
                            XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
                            thisStr = rtss.toString();
                        } catch (NumberFormatException ex) {
                            System.out.println("未找到对应的SST索引 '" + sstIndex + "': " + ex.toString());
                        }
                        break;

                    case NUMBER:
                        String n = value.toString();
                        //判断该数字是不是时间
                        if (HSSFDateUtil.isADateFormat(this.formatIndex, n)) {
                            Double d = Double.parseDouble(n);
                            Date date = HSSFDateUtil.getJavaDate(d);
                            thisStr = formatDateToString(date);
                        } else if (this.formatString != null)
                            thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex,
                                    this.formatString);
                        else
                            thisStr = n;
                        break;

                    //其他特殊类型可以自己扩展
                    default:
                        thisStr = "(尚未支持的类型: " + nextDataType + ")";
                        break;
                }
                if (thisStr == null || thisStr.equals(NULL)) {
                    isCellNull = true;
                }
                //这里可以根据实际业务进行改造，以单元格为单位处理
                record.add(curCol, thisStr);
                curCol++;
            } else if ("row".equals(name)) {
                if (isCellNull == false && record.size() != rowNull) {
                    if (MAX_COLUMN >= record.size()) {
                        int len = MAX_COLUMN - record.size();
                        for (int i = 0; i < len; i++) {
                            record.add(curCol, NULL);
                            curCol++;
                        }
                    }
                    //这里可以根据实际业务进行改造，以行为单位处理
                    rows.add(curRow, record);
                    isCellNull = false;
                    //这里必须新建对象，不能为了节约清空之前的对象
                    record = new ArrayList<String>();
                    curRow++;
                    curCol = 0;
                    rowNull = 0;
                }
            }
        }


        /**
         * 计算两个单元格中间的空单元格数量
         * <p>Excel2007文件最大行数为1048576，最大列数为16284，列名为XFD</P>
         *
         * @param ref    当前单元格名称
         * @param preRef 之前一个单元格名称
         * @return
         */
        private int countNullCell(String ref, String preRef) {
            String xfd = ref.replaceAll("\\d+", "");
            String xfd_1 = preRef.replaceAll("\\d+", "");

            xfd = fillChar(xfd, 3, '@', true);
            xfd_1 = fillChar(xfd_1, 3, '@', true);

            char[] letter = xfd.toCharArray();
            char[] letter_1 = xfd_1.toCharArray();
            int res = (letter[0] - letter_1[0]) * 26 * 26 + (letter[1] - letter_1[1]) * 26 + (letter[2] - letter_1[2]);
            return res - 1;
        }

        /**
         * 用特定字符填充字符串到指定长度
         *
         * @param str   原字符串
         * @param len   目标串的长度
         * @param let   填充使用的字符
         * @param isPre 是否在前面填充字符
         * @return
         */
        private String fillChar(String str, int len, char let, boolean isPre) {
            int len_1 = str.length();
            if (len_1 < len) {
                if (isPre) {
                    for (int i = 0; i < (len - len_1); i++) {
                        str = let + str;
                    }
                } else {
                    for (int i = 0; i < (len - len_1); i++) {
                        str = str + let;
                    }
                }
            }
            return str;
        }

        /**
         * 每遇到一个标签调用，用于记录标签类型
         *
         * @param ch     文件流字符数组
         * @param start  开始位置
         * @param length 读取长度
         * @return
         * @throws SAXException
         */
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (vIsOpen)
                value.append(ch, start, length);
        }

        /**
         * 根据单元格名称计算列数
         *
         * @param name 单元格名称(如:A1)
         * @return
         */
        private int nameToColumn(String name) {
            int column = -1;
            for (int i = 0; i < name.length(); ++i) {
                int c = name.charAt(i);
                column = (column + 1) * 26 + c - 'A';
            }
            return column;
        }

        /**
         * 将日期类型转变为String格式
         *
         * @param date 日期
         * @return
         */
        private String formatDateToString(Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date);
        }
    }

    //事先定义的最大列数
    private static int MAX_COLUMN = 7;

    private static String XLS = ".xls";
    private static String XLSX = ".xlsx";

    public ExcelEventParser() {
    }

    /**
     * 构造XLSXReader读取Excel内容
     *
     * @param styles           样式信息
     * @param strings          共享字符集
     * @param sheetInputStream sheet输入流
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private List<List<String>> XLSXProcessSheet(StylesTable styles, ReadOnlySharedStringsTable strings,
                                                InputStream sheetInputStream) throws IOException, ParserConfigurationException, SAXException {
        //将输入流包装成XML源
        InputSource sheetSource = new InputSource(sheetInputStream);
        //构造基于标签回调的XMLReader
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader sheetParser = saxParser.getXMLReader();
        //构造XLSXReader
        XLSXReader handler = new XLSXReader(styles, strings);
        //添加管理者
        sheetParser.setContentHandler(handler);
        //读入XML源
        sheetParser.parse(sheetSource);
        return handler.rows;
    }

    /**
     * 读取Excel2007文件
     *
     * @param filePath 文件路径
     * @return
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private List<List<String>> XLSXprocess(String filePath)
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        //将文件以压缩包的形式读入
        OPCPackage opcPackage = OPCPackage.open(filePath, PackageAccess.READ);
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opcPackage);
        XSSFReader xssfReader = new XSSFReader(opcPackage);
        List<List<String>> sheet = null;
        //读入样式信息
        StylesTable styles = xssfReader.getStylesTable();
        //构造sheet迭代器
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        //这里只读入了第一个sheet，如果需要读取多个可以遍历这个迭代器
        if (iter.hasNext()) {
            InputStream stream = iter.next();
            //构造XLSXReader读取内容
            sheet = XLSXProcessSheet(styles, strings, stream);
            //关闭流
            stream.close();
        }
        opcPackage.close();
        return sheet;
    }

    /**
     * 读取Excel2003文件
     *
     * @param filePath 文件路径
     * @return
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private List<List<String>> XLSprocess(String filePath)
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        XLSReader xls = new XLSReader();
        List<List<String>> sheet = xls.process(filePath);
        return sheet;
    }

    /**
     * 读取Excel文件内容并返回包含文件内容的List
     *
     * @param filePath 文件路径
     * @return
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static List<List<String>> readerExcel(String filePath, int maxColumn)
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        MAX_COLUMN = maxColumn;
        // 将文件数据储存起来
        List<List<String>> sheet = null;
        // 获取文件后缀名，判断文件类型
        int index = filePath.lastIndexOf(".");
        String excelVer = filePath.substring(index, filePath.length());
        ExcelEventParser csvReader = new ExcelEventParser();
        if (XLS.equals(excelVer)) {
            sheet = csvReader.XLSprocess(filePath);
        } else if (XLSX.equals(excelVer)) {
            sheet = csvReader.XLSXprocess(filePath);
        }
        return sheet;
    }

    public static void main(String[] args) {
        try {
            long startTime = System.currentTimeMillis();
            // 要导入的文件地址（例：D:/新建 Microsoft Excel 工作表 .xlsx）
            String filePath = "E:\\document\\company\\ywkj\\通宝行\\生鲜投保清单-5(无保单号).xlsx";
            List<List<String>> sheet = readerExcel(filePath, 17);
            long endTime = System.currentTimeMillis();
//            System.out.println("总共话费时间：" + (endTime - startTime) / 1000 + "s;总条数：" + sheet.size());
            for (List<String> row : sheet) {
                for (String cell : row) {
                    System.out.print(cell + ",");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
