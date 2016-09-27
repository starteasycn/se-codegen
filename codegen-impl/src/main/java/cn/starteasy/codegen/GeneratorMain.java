package cn.starteasy.codegen;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.provider.db.DataSourceProvider;
import cn.org.rapid_framework.generator.util.JdbcConstants;
import com.google.common.io.Files;
import com.mysql.jdbc.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * @author badqiu
 * @email badqiu(a)gmail.com
 */

public class GeneratorMain {
    public static final boolean isStandard = true;

    private static final Logger logger = LoggerFactory.getLogger(GeneratorMain.class);


    private static String getDbType(String url) {
        if (url.toLowerCase().indexOf("mysql") != -1) {
            return "mysql";
        } else if (url.toLowerCase().indexOf("oracle") != -1) {
            return "oracle";
        }
        return "mysql";
    }


    /**
     * 请直接修改以下代码调用不同的方法以执行相关生成任务.
     */
    public static void main(String[] args) throws Exception {

        String dbType = "mysql";
        String url = GeneratorProperties.getRequiredProperty("jdbc.url");
        dbType = getDbType(url);
        GeneratorFacade g = new GeneratorFacade();
        boolean isCreateProject = true;
        try {
            isCreateProject = GeneratorProperties.getRequiredProperty("isCreateProject").equals("true");
        }catch (Exception ex)
        {
            logger.error("请配置是否创建工程");
        }
        String outRoot = GeneratorProperties.getRequiredProperty("outRoot");
        String module = GeneratorProperties.getRequiredProperty("module");
        String gradle_version = "2";
//		String gradle_version = GeneratorProperties.getRequiredProperty("gradle_version");
//		if(StringUtils.isNullOrEmpty(gradle_version))
//		{
//			gradle_version = "2";
//		}

        g.deleteOutRootDir();            //删除生成器的输出目录g
        if (JdbcConstants.ORACLE.equals(dbType)) {
            g.generateByAllTable("templateOracle/mybatis");    //自动搜索数据库中的所有表并生成文件,template为模板的根目录
            if (isStandard) {
                g.generateByTableList("templateOracle/common");
            }
        } else {
            g.generateByAllTable("template/mybatis");    //自动搜索数据库中的所有表并生成文件,template为模板的根目录
            if (isStandard) {
                g.generateByTableList("template/common");
            }
        }

        String fileName ="";
        String projectDir = GeneratorProperties.getRequiredProperty("projectDir");
        String autoGenProject = projectDir;///managerui-biz-startup";
        String basePackage = GeneratorProperties.getRequiredProperty("basepackage");
        String basePackageDir = basePackage.replace(".", "/");

        String domain_projectPath = autoGenProject + "/" + module + "-domain";
        String service_projectPath = autoGenProject + "/" + module + "-service";
        String admin_projectPath = autoGenProject + "/" + module + "-admin-war";
        String api_projectPath = autoGenProject + "/" + module + "-api";
        String web_projectPath = autoGenProject + "/" + module + "-web-war";
        String isfirtstFlag = domain_projectPath + "/src/main/java/" + basePackageDir + "/domain";

        String isAdminInit = GeneratorProperties.getRequiredProperty("initAdminSql");//admin是否需要初始化数据库

        File f = new File(isfirtstFlag);
        if (f.exists() && isCreateProject) {
            isCreateProject = false;
        }
        if (isCreateProject) {
            //yq add 进行对应的文件拷贝
            //docs 下的多文件进行合并
//            File allDocsFile = new File(outRoot + "/" + module + "-domain/docs/all.html");
//            Appendable allDocsFileAppendable = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(allDocsFile)));
//			String initSqldir = outRoot+"/" + module + "-initdatasql";
            //domain拷贝
            //autosetup工程位置
            /*****
             * 复制domain
             */
            String parentDirStrTest = "";
            String parentDirStr = domain_projectPath + "/src/main/java/" + basePackageDir + "/domain";
            File parentDir = new File(parentDirStr);
            parentDir.mkdirs();
            File sourceDir = new File(outRoot + "/" + module + "-domain");
            if (sourceDir.listFiles() != null) {
                for (File file : sourceDir.listFiles()) {
                    if (file.isFile()) {
                        Files.copy(file, new File(parentDirStr + "/" + file.getName()));
                    }
//                    else if(file.getName().equals("docs")){
//                        mergeFile(file.getPath(), allDocsFileAppendable);
//
//                    }
                    else
                    {

                        copyDirectiory(file.getAbsolutePath(), parentDirStr + "/" + file.getName());
                    }
                }
            }

            //dao拷贝
            parentDirStr = service_projectPath + "/src/main/java/" + basePackageDir + "/dao";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();
            if (sourceDir.listFiles() != null) {
                sourceDir = new File(outRoot + "/" + module + "-service/java");
                for (File file : sourceDir.listFiles()) {
                    if(file.isDirectory()){
                        copyDirectiory(file.getPath(),parentDirStr+"/"+file.getName());
                    }else{
                        Files.copy(file, new File(parentDirStr + "/" + file.getName()));
                    }
                }
            }

            //mybatis拷贝
            parentDirStr = service_projectPath + "/src/main/resources/mybatis";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();
            if (sourceDir.listFiles() != null) {
                sourceDir = new File(outRoot + "/" + module + "-service/resources");
                for (File file : sourceDir.listFiles()) {
                    if(file.isDirectory()){
                        copyDirectiory(file.getPath(),parentDirStr+"/"+file.getName());
                    }else{
                        Files.copy(file, new File(parentDirStr + "/" + file.getName()));
                    }
                }
            }

            //dubbo拷贝
            parentDirStr = service_projectPath + "/src/main/resources/dubbo";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();
            sourceDir = new File(outRoot + "/dubbo");
            for (File file : sourceDir.listFiles()) {
                Files.copy(file, new File(parentDirStr + "/" + file.getName()));
            }


            //api拷贝
            parentDirStrTest = service_projectPath + "/src/test/java/" + basePackageDir + "/service/impl";
            parentDirStr = service_projectPath + "/src/main/java/" + basePackageDir + "/service";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();
            sourceDir = new File(outRoot + "/" + module + "-api");
            if (sourceDir.listFiles() != null) {
                for (File file : sourceDir.listFiles()) {
                    if(file.isDirectory()){
                        if(!file.getName().endsWith("test")) {
                            copyDirectiory(file.getPath(),parentDirStr+"/"+file.getName());
                        }else{
                            copyDirectiory(file.getPath(),parentDirStrTest+"/"+file.getName());
                        }
                    }else{
                        Files.copy(file, new File(parentDirStr + "/" + file.getName()));
                    }
                }
            }


            //facade拷贝
            parentDirStrTest = service_projectPath + "/src/test/java/" + basePackageDir + "/facade/impl";
            parentDirStr = service_projectPath + "/src/main/java/" + basePackageDir + "/facade";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();
            sourceDir = new File(outRoot + "/" + module + "-facade");
            if (sourceDir.listFiles() != null) {
                for (File file : sourceDir.listFiles()) {
                    if(file.isDirectory()){
                        if(file.getName().endsWith("test")){
                            copyDirectiory(file.getPath(),parentDirStrTest+ "/" + file.getName());
                        }else {
                            copyDirectiory(file.getPath(),parentDirStr+ "/" + file.getName());
                        }
                    }else{
                        Files.copy(file, new File(parentDirStr + "/" + file.getName()));
                    }
                }
            }

            //war-maps拷贝
            parentDirStr = admin_projectPath + "/src/main/java/" + basePackageDir + "/common";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();
            sourceDir = new File(outRoot + "/" + module + "-common");
            if (sourceDir.listFiles() != null) {
                for (File file : sourceDir.listFiles()) {
                    Files.copy(file, new File(parentDirStr + "/" + file.getName()));
                }
            }

            //war-controller拷贝
            parentDirStr = admin_projectPath + "/src/main/java/" + basePackageDir + "/controller";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();
            sourceDir = new File(outRoot + "/" + module + "-controller");
            if (sourceDir.listFiles() != null) {
                for (File file : sourceDir.listFiles()) {
                    if(file.isDirectory()){
                        copyDirectiory(file.getPath(),parentDirStr+"/" + file.getName());
                    }else{
//                        if (!"RoleController".equals(file.getName()) || !"UserController".equals(file.getName())) {
                            Files.copy(file, new File(parentDirStr + "/" + file.getName()));
//                        }
                    }
                }
            }

            //war-filter拷贝
            parentDirStr = admin_projectPath + "/src/main/java/" + basePackageDir + "/filter";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();
            sourceDir = new File(outRoot + "/" + module + "-filter");
            if (sourceDir.listFiles() != null) {
                for (File file : sourceDir.listFiles()) {
                    Files.copy(file, new File(parentDirStr + "/" + file.getName()));
                }
            }

            //war-ftl拷贝
            parentDirStr = admin_projectPath + "/src/main/webapp/WEB-INF/view/module";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();
            sourceDir = new File(outRoot + "/" + module + "-view");
            if (sourceDir.listFiles() != null) {
                for (File file : sourceDir.listFiles()) {
                    if (!file.isDirectory()) {
                        if (!"role.ftl".equals(file.getName()) || !"user.ftl".equals(file.getName())) {
                            Files.copy(file, new File(parentDirStr + "/" + file.getName()));
                        }
                    }
                }
            }

            //war-custome-ftl拷贝
            parentDirStr = admin_projectPath + "/src/main/webapp/WEB-INF/view/module/custome";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();
            sourceDir = new File(outRoot + "/" + module + "-view/custome");
            if (sourceDir.listFiles() != null) {
                for (File file : sourceDir.listFiles()) {
                    if (!file.isDirectory()) {
                        if (!"role_page_grid.ftl".equals(file.getName()) || !"user_page_grid.ftl".equals(file.getName())) {
                            Files.copy(file, new File(parentDirStr + "/" + file.getName()));
                        }
                    }
                }
            }

            //war-custome-script-ftl拷贝
            parentDirStr = admin_projectPath + "/src/main/webapp/WEB-INF/view/module/custome/script";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();
            sourceDir = new File(outRoot + "/" + module + "-view/custome/script");
            if (sourceDir.listFiles() != null) {
                for (File file : sourceDir.listFiles()) {
                    if (!file.isDirectory()) {
                        //if(!"role_biz_script.ftl".equals(file.getName()) || !"user_biz_script.ftl".equals(file.getName())) {
                        Files.copy(file, new File(parentDirStr + "/" + file.getName()));
                        //}
                    }
                }
            }

            //war-custome-js-ftl拷贝
            parentDirStr = admin_projectPath + "/src/main/webapp/WEB-INF/view/module/custome/js";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();
            sourceDir = new File(outRoot + "/" + module + "-view/custome/js");
            if (sourceDir.listFiles() != null) {
                for (File file : sourceDir.listFiles()) {
                    if (!file.isDirectory()) {
                        //if(!"biz_role.ftl".equals(file.getName()) || !"UserController".equals(file.getName())) {
                        Files.copy(file, new File(parentDirStr + "/" + file.getName()));
                        //}
                    }
                }
            }

            /*****
             * 复制api
             */
            parentDirStr = api_projectPath + "/src/main/java/" + basePackageDir + "/api";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();

            /*****
             * 复制web-war
             */
            String tempParentDirStr = web_projectPath + "/src/main/java/" + basePackageDir;
            parentDirStr = tempParentDirStr + "/controller";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();
            //复制controller
            copyDirectiory(outRoot + "/" + module + "-web-controller", parentDirStr);

            //拷贝resources
            parentDirStr = web_projectPath + "/src/main/resources";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();

            copyDirectiory(outRoot + "/web/resources", parentDirStr);


            //复制 common
            parentDirStr = tempParentDirStr + "/common";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();

            sourceDir = new File(outRoot + "/" + module + "-common");
            if (sourceDir.listFiles() != null) {
                for (File file : sourceDir.listFiles()) {
                    if (!file.isDirectory()) {
                        //不拷贝  ServiceMaps.java
                        if (!"ServiceMaps.java".equals(file.getName())) {
                            Files.copy(file, new File(parentDirStr + "/" + file.getName()));
                        }
                    }
                }
            }

            //复制 filter
            parentDirStr = tempParentDirStr + "/filter";
            parentDir = new File(parentDirStr);
            parentDir.mkdirs();

            sourceDir = new File(outRoot + "/" + module + "-filter");
            if (sourceDir.listFiles() != null) {
                for (File file : sourceDir.listFiles()) {
                    if (!file.isDirectory()) {
                        //不拷贝  ServiceMaps.java
                        if (!"ServiceMaps.java".equals(file.getName())) {
                            Files.copy(file, new File(parentDirStr + "/" + file.getName()));
                        }
                    }
                }
            }

            //web-war 增加WEB-INF  web_projectPath + "/src/main/java/"
            copyDirectiory(outRoot + "/webwar-config", web_projectPath + "/src/main/webapp");
            copyDirectiory(outRoot + "/adminwar-config", admin_projectPath + "/src/main/webapp");

            //复制build.gradle
            copyBuildGradle(outRoot, "domain", domain_projectPath);
            copyBuildGradle(outRoot, "admin", admin_projectPath);
            copyBuildGradle(outRoot, "api", api_projectPath);
            copyBuildGradle(outRoot, "service", service_projectPath);
            copyBuildGradle(outRoot, "web", web_projectPath);
            //项目build.gradle
            copyBuildGradleProjectGradle(outRoot, "project", projectDir, gradle_version);

            //copy-test
            copyDirectiory(outRoot + "/test", admin_projectPath + "/src/test");
            copyDirectiory(outRoot + "/test", web_projectPath + "/src/test");
            //copy-admin-resource
            copyDirectiory(outRoot + "/admin/resources", admin_projectPath + "/src/main/resources");
            copyDirectiory(outRoot + "/service/resources", service_projectPath + "/src/main/resources");
            try {
                if (System.getProperty("os.name").startsWith("Windows")) {
                    copyDirectiory(outRoot.substring(0, outRoot.replaceAll("/","\\").lastIndexOf("\\")) + "/common/webapp", admin_projectPath + "/src/main/webapp");

                } else {
                    copyDirectiory(outRoot.substring(0, outRoot.lastIndexOf("/")) + "/common/webapp", admin_projectPath + "/src/main/webapp");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("请检查你的配置文件outRoot,请配置你的outRoot到codegen目录/generator-output");
            }

            /*****
             * 插入admin初始数据
             */
            if(Boolean.TRUE.toString().equalsIgnoreCase(isAdminInit)){
                String initSqldir = outRoot + "/" + module + "-initdatasql";
                List<String> list = insertResuorce(initSqldir);
                insertSql(list, module);
            }

        }

        Thread.sleep(20000);
//		g.generateByAllTable("template/hibernate");
//		g.generateByClass(Blog.class,"template_clazz");

//		g.deleteByTable("table_name", "template"); //删除生成的文件
        //打开文件夹
        //Runtime.getRuntime().exec("cmd.exe /c start "+GeneratorProperties.getRequiredProperty("outRoot"));

    }

    private static void copyBuildGradle(String outRoot, String name, String destDir) {
        File sourceDir = new File(outRoot + "/" + name);
        if (sourceDir.listFiles() != null) {
            for (File file : sourceDir.listFiles()) {
                if (!file.isDirectory()) {
                    if (file.getName().endsWith(".gradle") || file.getName().endsWith("properties")) {
                        //if(!"biz_role.ftl".equals(file.getName()) || !"UserController".equals(file.getName())) {
                        try {
                            Files.copy(file, new File(destDir + "/" + file.getName()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //}
                    }
                }
            }
        }
    }


    private static void copyBuildGradleProjectGradle(String outRoot, String name,
                                                     String destDir, String gradleVersion) {
        File sourceDir = new File(outRoot + "/" + name);
        String version = "v" + gradleVersion;
        if (sourceDir.listFiles() != null) {
            for (File file : sourceDir.listFiles()) {
                if (!file.isDirectory()) {
                    String fileName = file.getName();

                    if (fileName.indexOf("v") != -1) {
                        if (fileName.indexOf(version) == -1) {
                            continue;
                        } else {
                            fileName = fileName.replace(version, "");
                        }
                    }
                    //if(!"biz_role.ftl".equals(file.getName()) || !"UserController".equals(file.getName())) {
                    try {
                        Files.copy(file, new File(destDir + "/" + fileName));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //}

                }
            }
        }
    }

    private static void mergeFile(String sourceDir, Appendable targetDir) throws IOException {
        for(File file:(new File(sourceDir)).listFiles()){
            Files.copy(file, Charset.defaultCharset(), targetDir);
            ((Flushable) targetDir).flush();
        }

    }

    private static void copyDirectiory(String sourceDir, String targetDir) throws IOException {

        //新建目标目录

        (new File(targetDir)).mkdirs();

        //获取源文件夹当下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();

        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                //源文件
                File sourceFile = file[i];
                if (!("RoleController.java".equals(sourceFile.getName()) || "UserController".equals(sourceFile.getName()))) {
                    //目标文件
                    File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                    Files.copy(sourceFile, targetFile);
                }
            } else if (file[i].isDirectory()) {
                //准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                //准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();

                copyDirectiory(dir1, dir2);
            }
        }

    }


    private static List<String> insertResuorce(String initSqldir) {
        String initSqldirFile = initSqldir + "/init.sql";
        List<String> sqlList = new ArrayList<String>();

        try {
            InputStream sqlFileIn = new FileInputStream(initSqldirFile);

            StringBuffer sqlSb = new StringBuffer();
            byte[] buff = new byte[1024];
            int byteRead = 0;
            while ((byteRead = sqlFileIn.read(buff)) != -1) {
                sqlSb.append(new String(buff, 0, byteRead));
            }

            // Windows 下换行是 /r/n, Linux 下是 /n
            String[] sqlArr = sqlSb.toString().split(";");
            for (int i = 0; i < sqlArr.length; i++) {
                String sql = sqlArr[i].replaceAll("--.*", "").trim();
                if (!sql.equals("")) {
                    sqlList.add(sql);
                }
            }
            return sqlList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String  getRoleResourceId(String tablePrefix)
    {
        String sql = "select id from " + tablePrefix + "_resource where bizModelName='role' ";
        return sql;

    }

    private static String createRoleResourceAction(String tablePrefix,String resourceId)
    {
        String sql = "INSERT INTO `" + tablePrefix + "_resource_action` (`resourceId`,`name`,`actionAlias`,`creator`,`createDate`,`lastModifier`,`lastModDate`,`description`,`status`)" +
                "      VALUES("+resourceId+",'分配资源','resource_assign',0,1451975928507,0,1451975928507,'分配资源',0)";
        return sql;
    }

    private static void insertSql(List<String> sqlList, String tablePrefix) {
        List<String> list_menu = new ArrayList<String>();
        String role = "insert into " + tablePrefix + "_role (id, `name` ,  `description` ,`status`," +
                "`creator` ,`createDate` ,`lastModifier`  ,`lastModDate` ) values(1,'超级管理员','超级管理员',0,0,0,0,0)";
        String roleUser = "insert into " + tablePrefix + "_role_user(`status` ,`userId`,`roleId` ," +
                "`creator` ,`createDate` ,`lastModifier`  ,`lastModDate`)values(0,1,1,0,0,0,0)";
        String menu = "insert into " + tablePrefix + "_role_resource ( `status`, `resourceId` , `resourceActionId`, `roleId`," +
                "`creator` ,`createDate` ,`lastModifier`  ,`lastModDate`  )" +
                "select 0,id,0,1,`creator` ,`createDate` ,`lastModifier`  ,`lastModDate` from " + tablePrefix + "_resource";
        String menuAction = "insert into " + tablePrefix + "_role_resource (`status`,`resourceId` ,`resourceActionId`,`roleId`," +
                "`creator` ,`createDate` ,`lastModifier`  ,`lastModDate`  )" +
                "select 0,resourceId,id,1,`creator` ,`createDate` ,`lastModifier`  ,`lastModDate` from " + tablePrefix + "_resource_action";
        list_menu.add(role);
        list_menu.add(roleUser);
        list_menu.add(menu);
        list_menu.add(menuAction);

        //按新的方式处理
        Connection conn = null;
//		Connection schemaConn = DataSourceProvider.getSchemaConnection();
//		DatabaseMetaData dbMetaData = conn.getMetaData();
//		ResultSet rs = dbMetaData.getTables(conn.getCatalog(), getSchema(), null, null);

        Statement stmt = null;
        try {
            conn = DataSourceProvider.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            for (String sql : sqlList) {
                System.out.println(sql);
                stmt.addBatch(sql);
                stmt.executeBatch();
            }
            ResultSet rs =  stmt.executeQuery(getRoleResourceId(tablePrefix));
            String id = "";
            if(rs.next())
            {
                id = rs.getString(1);
            }
            if(!StringUtils.isNullOrEmpty(id))
            {
                stmt.addBatch(createRoleResourceAction(tablePrefix,id));
                stmt.executeBatch();
            }
            for (String sql : list_menu) {
                System.out.println(sql);
                stmt.addBatch(sql);
                stmt.executeBatch();
            }
//			int[] rows = stmt.executeBatch();
//			System.out.println("Row count:" + Arrays.toString(rows));
            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private void copyWebFrontProject() {

    }


    private void initUser() {

    }


}

