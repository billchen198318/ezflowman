<#if driverType == "1">
  select top 24 * from ${mainTbl} where ${efgpProcessStatusField}='1' and ${efgpProcessNoField} is not null and ${efgpProcessNoField} <> ''
<#elseif driverType == "2">
  select * from ${mainTbl} where ${efgpProcessStatusField}='1' and ${efgpProcessNoField} is not null and ${efgpProcessNoField} <> '' and ROWNUM <= 24
<#else>
  select * from ${mainTbl} where ${efgpProcessStatusField}='1' and ${efgpProcessNoField} is not null and ${efgpProcessNoField} <> '' limit 24
</#if>