<#if driverType == "1">
  update ${mainTbl} set ${efgpProcessStatusField} = :stateVal, ${efgpProcessNoField} = :processNoVal where ${mainTblPkField} = :mainTblPkFieldVal
<#elseif driverType == "2">
  update ${mainTbl} set ${efgpProcessStatusField} = :stateVal, ${efgpProcessNoField} = :processNoVal where ${mainTblPkField} = :mainTblPkFieldVal
<#else>
  update ${mainTbl} set ${efgpProcessStatusField} = :stateVal, ${efgpProcessNoField} = :processNoVal where ${mainTblPkField} = :mainTblPkFieldVal
</#if>