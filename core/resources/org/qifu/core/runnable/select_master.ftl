<#if driverType == "1">
  select top 8 * from ${mainTbl} where ${efgpProcessStatusField} is null or ${efgpProcessStatusField}='0'
<#elseif driverType == "2">
  select * from ${mainTbl} where ${efgpProcessStatusField} is null or ${efgpProcessStatusField}='0' and ROWNUM <= 8
<#else>
  select * from ${mainTbl} where ${efgpProcessStatusField} is null or ${efgpProcessStatusField}='0' limit 8
</#if>