<#if driverType == "1">
  select * from ${dtlTbl} where ${mstFieldName} = :dtlFieldValue
<#elseif driverType == "2">
  select * from ${dtlTbl} where ${mstFieldName} = :dtlFieldValue
<#else>
  select * from ${dtlTbl} where ${mstFieldName} = :dtlFieldValue
</#if>