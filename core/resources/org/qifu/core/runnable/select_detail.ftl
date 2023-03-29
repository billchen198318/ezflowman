<#if driverType == "1">
  select * from ${dtlTbl} where ${dtlFieldName} = :mstFieldValue
<#elseif driverType == "2">
  select * from ${dtlTbl} where ${dtlFieldName} = :mstFieldValue
<#else>
  select * from ${dtlTbl} where ${dtlFieldName} = :mstFieldValue
</#if>