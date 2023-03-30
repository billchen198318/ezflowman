findFormOIDsOfProcess 有可能帶回多筆 oid 所以取最後一筆

例如回傳多筆, 如下
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
   <soapenv:Body>
      <ns1:findFormOIDsOfProcessResponse soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" xmlns:ns1="http://webservice.nana.dsc.com/">
         <findFormOIDsOfProcessReturn xsi:type="xsd:string">0af5253cefaf100484d196fddccb88ca,bc09f160efb2100484d196fddccb88ca</findFormOIDsOfProcessReturn>
      </ns1:findFormOIDsOfProcessResponse>
   </soapenv:Body>
</soapenv:Envelope>


代號
PKG16777252197981


---------------------------------------------------------------------------------------------------------------------


<soapenv:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:web="http://webservice.nana.dsc.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <web:fetchProcInstanceWithSerialNo soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
         <pProcessInstanceSerialNo xsi:type="xsd:string">TIPTOPPROCESSPKG_apmt59000012525</pProcessInstanceSerialNo>
      </web:fetchProcInstanceWithSerialNo>
   </soapenv:Body>
</soapenv:Envelope>



<com.dsc.nana.services.webservice.SimpleProcessInfo>
  <processId>TIPTOPPROCESSPKG_apmt590</processId>
  <processName>委外採購單維護作業</processName>
  <createdTime>2021-04-22 07:47:58.0</createdTime>
  <requesterId>T0529</requesterId>
  <requesterName>Tester</requesterName>
  <state>closed.completed</state>
  <OID>0001ab55ea9710048b9316b69b3a8591</OID>
  <serialNo>TIPTOPPROCESSPKG_apmt59000012525</serialNo>
  <subject>TIPTOP Plant ID:GTAA 委外採購單維護作業 AV23-21040191</subject>
  <abortComment></abortComment>
</com.dsc.nana.services.webservice.SimpleProcessInfo>



<com.dsc.nana.services.webservice.SimpleProcessInfo>
  <processId>Payment_Apply</processId>
  <processName>通用申請單</processName>
  <createdTime>2023-01-05 17:32:53.0</createdTime>
  <requesterId>T0088</requesterId>
  <requesterName>Frank</requesterName>
  <state>open.running</state>
  <OID>1482898cef7d100484d196fddccb88ca</OID>
  <serialNo>Payment_Apply00000561</serialNo>
  <subject>名稱：GEN 通用表單 單號：GEN-2023010001   申請人： T0088_Frank  部門：資訊中心(Y)   公司：B</subject>
  <abortComment></abortComment>
</com.dsc.nana.services.webservice.SimpleProcessInfo>


1 = open.running 		開單執行中
3 = closed.completed  	流程完成結束
4 = closed.aborted 		取消流程
5 = closed.terminated 	中止流程


---------------------------------------------------------------------------------------------------------------------


