<#if driverType == "1">
  update ${mainTbl} set ${efgpProcessStatusField} = :stateVal where ${efgpProcessNoField} = :pProcessInstanceSerialNo
<#elseif driverType == "2">
  update ${mainTbl} set ${efgpProcessStatusField} = :stateVal where ${efgpProcessNoField} = :pProcessInstanceSerialNo
<#else>
  update ${mainTbl} set ${efgpProcessStatusField} = :stateVal where ${efgpProcessNoField} = :pProcessInstanceSerialNo
</#if>