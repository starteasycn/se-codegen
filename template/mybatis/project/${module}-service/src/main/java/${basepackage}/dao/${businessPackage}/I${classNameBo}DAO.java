<#assign className = table.classNameBo>
/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: ${module}
 * $Id:  ${className}DAO.java ${now?string('yyyy-MM-dd HH:mm:ss')} $
 */
<#assign classNameLower = className?uncap_first>
<#assign hasBusinessPackage = false>
<#if (table.businessPackage!="")>
<#assign hasBusinessPackage = true>
</#if>
<#if hasBusinessPackage>
package ${basepackage}.${persistence}.${table.businessPackage};
</#if>
<#if !hasBusinessPackage>
package ${basepackage}.${persistence};
</#if>


import cn.thinkjoy.common.dao.IBaseDAO;

<#if hasBusinessPackage>
import ${basepackage}.domain.${table.businessPackage}.${className};
</#if>
<#if !hasBusinessPackage>
import ${basepackage}.domain.${className};
</#if>


public interface I${className}DAO extends IBaseDAO<${className}>{
	
<#list table.columns as column>
	<#if (column.unique && !column.pk)>
	${className} findBy${column.columnName}(${column.possibleShortJavaType} ${column.columnNameFirstLower});
	</#if>
</#list>

}
