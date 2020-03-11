package com.xusy.springbt.util.poi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;


//读取excel最大行数
public class MaxRowHandler {

    public static final String Excel_03 = ".xls";

    public static final String Excel_07 = ".xlsx";

    class XLSXMaxRowHandler extends DefaultHandler {

        //最大行数
        private int maxRow = 0;

        private int cellNum = 0;

        public void endElement(String uri, String localName, String name)
                throws SAXException {

            if ("row".equals(name)) {
                if (cellNum > 0) {
                    maxRow++;
                }
                cellNum = 0;
            }
        }
    }

    class XLSMaxRowHandler implements HSSFListener {

        private POIFSFileSystem fs;
        //最大行数
        private int maxRow = 0;

        public int process(String filePath) throws IOException {

            this.fs = new POIFSFileSystem(new FileInputStream(filePath));
            MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
            HSSFEventFactory factory = new HSSFEventFactory();
            HSSFRequest request = new HSSFRequest();
            //只监听RowRecord一种
            request.addListener(listener, RowRecord.sid);
            factory.processWorkbookEvents(request, fs);
            return maxRow;
        }

        /**
         * 处理Excel2003文件并计算行数
         * @param record
         * @return
         * @throws IOException
         */
        public void processRecord(Record record) {
            if (record.getSid() == RowRecord.sid) {
                maxRow++;
            }
        }
    }

    public MaxRowHandler() {
    }

    /**
     * 读取Excel2003文件最大行数
     * @param filePath 文件路径
     * @return
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public int maxXLSprocess(String filePath) throws IOException, OpenXML4JException,
            ParserConfigurationException, SAXException {
        XLSMaxRowHandler xls = new XLSMaxRowHandler();
        int num = xls.process(filePath);
        return num;
    }

    /**
     * 读取Excel2007文件最大行数
     * @param filePath 文件路径
     * @return
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private int maxXLSXprocess(String filePath) throws IOException, OpenXML4JException,
            ParserConfigurationException, SAXException {
        //将文件以压缩包的形式读入
        OPCPackage opcPackage = OPCPackage.open(filePath, PackageAccess.READ);
        XSSFReader xssfReader = new XSSFReader(opcPackage);
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        InputStream stream = iter.next();
        InputSource sheetSource = new InputSource(stream);
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader sheetParser = saxParser.getXMLReader();
        XLSXMaxRowHandler handler = new XLSXMaxRowHandler();
        sheetParser.setContentHandler(handler);
        sheetParser.parse(sheetSource);
        int num = handler.maxRow;
        stream.close();
        opcPackage.close();
        return num;
    }

    /**
     * 获取最大行数不再区分sheet和空行等
     * @param filePath 文件路径
     * @return
     */
    public static int getMaxRow(String filePath) {
        int index = filePath.lastIndexOf(".");
        String excelVer = filePath.substring(index, filePath.length());
        int num = 0;
        try {
            MaxRowHandler csvReader = new MaxRowHandler();
            if (".xls".equals(excelVer)) {
                num = csvReader.maxXLSprocess(filePath);
            } else if (".xlsx".equals(excelVer)) {
                num = csvReader.maxXLSXprocess(filePath);
            }
            return num;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        try {
            // 要导入的文件地址（例：D:/新建 Microsoft Excel 工作表 .xlsx）
            String filePath = "D:/新建 Microsoft Excel 工作表 .xlsx";
            int max = getMaxRow(filePath);
            System.out.println(max);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
