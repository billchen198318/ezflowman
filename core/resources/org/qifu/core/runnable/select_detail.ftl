<#if driverType == "1">
  select * from ${dtlTbl} where ${dtlFieldName} = :${mstFieldName}
<#elseif driverType == "2">
  select * from ${dtlTbl} where ${dtlFieldName} = :${mstFieldName}
<#else>
  select * from ${dtlTbl} where ${dtlFieldName} = :${mstFieldName}
</#if>