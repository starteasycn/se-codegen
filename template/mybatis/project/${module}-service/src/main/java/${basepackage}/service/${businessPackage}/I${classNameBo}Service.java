<#assign className = table.classNameBo>
<#assign classNameLower = className?uncap_first>
<#include "/macro.include"/>
/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: ${module}
 * $Id:  ${className}Service.java ${now?string('yyyy-MM-dd HH:mm:ss')} $
 */
<#assign hasBusinessPackage = false>
<#if (table.businessPackage!="")>
<#assign hasBusinessPackage = true>
</#if>
<#if hasBusinessPackage>
package ${basepackage}.service.${table.businessPackage};
</#if>
<#if !hasBusinessPackage>
package ${basepackage}.service;
</#if>
import cn.starteasy.core.common.service.IBaseService;
import cn.starteasy.core.common.service.IPageService;
<#if hasBusinessPackage>
import ${basepackage}.dao.${table.businessPackage}.I${className}DAO;
import ${basepackage}.domain.${table.businessPackage}.${className};
</#if>
<#if !hasBusinessPackage>
import ${basepackage}.dao.I${className}DAO;
import ${basepackage}.domain.${className};
</#if>

public interface I${className}Service extends IBaseService<I${className}DAO, ${className}>,IPageService<I${className}DAO, ${className}>{

}
