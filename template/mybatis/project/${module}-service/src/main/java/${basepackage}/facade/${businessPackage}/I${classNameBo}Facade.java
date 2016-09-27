<#assign className = table.classNameBo>
<#assign classNameLower = className?uncap_first>
<#assign classNameAllLower = table.classNameBo?lower_case>
/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: ${module}
 * $Id:  ${className}Facade.java ${now?string('yyyy-MM-dd HH:mm:ss')} $
 */
<#include "/macro.include"/>
<#assign hasBusinessPackage = false>
<#if (table.businessPackage!="")>
<#assign hasBusinessPackage = true>
</#if>
<#if hasBusinessPackage>
package ${basepackage}.facade.${table.businessPackage};
</#if>
<#if !hasBusinessPackage>
package ${basepackage}.facade;
</#if>

import cn.thinkjoy.common.service.IPersistenceProvider;

public interface I${className}Facade extends IPersistenceProvider{

}
