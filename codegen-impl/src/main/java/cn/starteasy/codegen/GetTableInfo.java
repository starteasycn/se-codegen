package cn.starteasy.codegen;

import cn.org.rapid_framework.generator.provider.db.table.TableFactory;
import cn.org.rapid_framework.generator.provider.db.table.model.Column;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by wangqinlei on 15/12/1.
 */
public class GetTableInfo {

    /**
     * 请直接执行相关生成表结构任务.
     */
    public static void main(String[] args) throws Exception {

			/*List<Table> tables = TableFactory.getInstance().getAllTables();
			for (Table t : tables){
				System.out.print(t.getSqlName());
				System.out.print(t.getName());
				System.out.println();
				LinkedHashSet<Column> columnList =  t.getColumns();
				for (Column column : columnList){
					System.out.print(column.getSqlName());
					System.out.print(column.getSqlTypeName());
					System.out.print(column.getSize());
					System.out.print(column.getRemarks());
					System.out.print(column.isPk()?"是":"否");
					System.out.println();
				}
				System.out.println();
			}*/

        // 生成表结构信息并html展示
        createTableInfoHtml();
    }

    private static void createTableInfoHtml() {
        // 获取表结构信息
        List<Table> tables = TableFactory.getInstance().getAllTables();

        StringBuilder sb = new StringBuilder();

        PrintStream printStream = null;
        try {
            // 生成文件
            printStream = new PrintStream(new FileOutputStream("tableInfo.html"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 拼装信息
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>表结构信息</title>");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        sb.append("<style type=\"text/css\">");
        sb.append("TABLE{border-collapse:collapse;border-left:solid 1 #000000; border-top:solid 1 #000000;padding:5px;}");
        sb.append("TH{border-right:solid 1 #000000;border-bottom:solid 1 #000000;}");
        sb.append("TD{font:normal;border-right:solid 1 #000000;border-bottom:solid 1 #000000;}");
        sb.append("</style></head>");
        sb.append("<body>");
        sb.append("<div align=\"left\">");
        sb.append("<br/>");
        sb.append("<br/>");

        for (Table t : tables) {

            // 模型名称|模型菜单名称|模型父菜单名称|继承体系  换行 模型描述    示例：'市数据|市管理|地域信息管理|CreateBaseDomain\n市基本信息'
            sb.append("<span>表名：").append(t.getSqlName()).append("(")
                    .append("模型名称:").append(t.getName()).append(",模型菜单名称:").append(t.getResName())
                    .append(",模型父菜单名称:").append(t.getParentResName()).append(",继承体系:").append(t.getParentClassName())
                    .append(",模型描述:").append(t.getDescription()).append(")</span>");

            sb.append("<table border=\"1\"><tr><th>列名</th><th>类型</th><th>长度</th><th>是否主键</th><th>说明</th><th>备注</th></tr>");

            LinkedHashSet<Column> columnList = t.getColumns();
            for (Column column : columnList) {
                sb.append("<tr>").append("<td>").append(column.getSqlName()).append("</td>")
                        .append("<td>").append(column.getSqlTypeName()).append("</td>")
                        .append("<td>").append(column.getSize()).append("</td>")
                        .append("<td>").append(column.isPk() ? "是" : "").append("</td>")
                        .append("<td>").append(column.getName()).append("</td>")
                        .append("<td>").append(column.getRemarks().split("\\|").length > 1 ? column.getDescription() : "")
                        .append("</td>").append("</tr>");
            }
            sb.append("</table>").append("<br/>");
        }

        sb.append("</div></body></html>");

        // 打印信息
        printStream.println(sb.toString());
        //System.out.println(sb.toString());
    }
}
