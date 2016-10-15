<#macro mapperEl_include value>${r'<#include "'}${value}${r'">'}</#macro>
<#assign classNameAllLower = table.classNameBo?lower_case>
<!DOCTYPE html>
<html lang="en">
<head>
<@mapperEl_include "../head.html" /></head>

<body class="gray-bg">
<#--前端表格-->
<@mapperEl_include "custome/${classNameAllLower}_page_grid.ftl" />
<#--js部分-->
<@mapperEl_include "custome/script/${classNameAllLower}_biz_script.ftl" />

</body>

