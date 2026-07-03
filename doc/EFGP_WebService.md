# NaNaWeb EFGP WorkflowService SOAP 文件

本文只整理 NaNaWeb / EFGP 的 `WorkflowService`。重點是如何拋單、如何取得 Lv1 manager，以及拋單前後會用到的 WebService。

## 1. Service URL

正式呼叫 URL 格式：

```text
http://{EFGP_HOST}:{PORT}/{SITE_NAME}/services/WorkflowService
```

範例：

```text
http://127.0.0.1:8080/NaNaWeb/services/WorkflowService
http://192.168.10.19:8086/NaNaWeb/services/WorkflowService
```

WSDL URL：

```text
http://{EFGP_HOST}:{PORT}/{SITE_NAME}/services/WorkflowService?wsdl
```

## 2. SOAP 共通設定

```text
SOAP Version : SOAP 1.1
Style        : RPC
Use          : encoded
Namespace    : http://webservice.nana.dsc.com/
SOAPAction   : 空字串
Content-Type : text/xml; charset=utf-8
```

共通 Envelope：

```xml
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:web="http://webservice.nana.dsc.com/"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <soapenv:Header/>
  <soapenv:Body>
    <!-- method body -->
  </soapenv:Body>
</soapenv:Envelope>
```

## 3. 標準拋單呼叫流程

```text
fetchOrgUnitOfUserId
  -> findManagerByAppLvl          # 若表單或流程需要先取得 Lv1 manager
  -> findFormOIDsOfProcess
  -> getFormFieldTemplate
  -> invokeProcess
  -> fetchProcInstanceWithSerialNo
```

真正建立流程的是 `invokeProcess`。

## 4. fetchOrgUnitOfUserId - 取得申請人組織單位

拋單時 `invokeProcess` 需要 `pOrgUnitId`，通常先用申請人帳號查組織單位 OID。

URL：

```text
POST http://{EFGP_HOST}:{PORT}/{SITE_NAME}/services/WorkflowService
```

SOAP Method：

```text
fetchOrgUnitOfUserId
```

入參：

| 參數 | 型別 | 必填 | 說明 |
|---|---|---:|---|
| `pUserId` | string | Y | 使用者帳號/員工代號，例如 `T0088` |

出參：

| 回傳節點 | 型別 | 說明 |
|---|---|---|
| `fetchOrgUnitOfUserIdReturn` | string XML | 使用者所屬組織單位清單 |

回傳 XML 常用欄位：

| 欄位 | 說明 |
|---|---|
| `OID` | 組織單位 OID，拋單時作為 `pOrgUnitId` |
| `id` | 組織單位代號 |
| `name` | 組織單位名稱 |
| `orgName` | 所屬公司/組織名稱 |
| `isMain` | 是否主要部門 |

Request 範例：

```xml
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:web="http://webservice.nana.dsc.com/"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <soapenv:Header/>
  <soapenv:Body>
    <web:fetchOrgUnitOfUserId soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
      <pUserId xsi:type="xsd:string">T0088</pUserId>
    </web:fetchOrgUnitOfUserId>
  </soapenv:Body>
</soapenv:Envelope>
```

Response 核心內容範例：

```xml
<organizationUnits>
  <com.dsc.nana.data__transfer.OrganizationUnitForInvokingListDTO>
    <OID>ORG_UNIT_OID</OID>
    <id>IT</id>
    <name>資訊部</name>
    <orgName>GTAC</orgName>
    <isMain>true</isMain>
  </com.dsc.nana.data__transfer.OrganizationUnitForInvokingListDTO>
</organizationUnits>
```

## 5. findManagerByAppLvl - 取得 Lv1 manager / 等層主管

EFGP 這支不是固定用數字 `Lv1` 查詢，而是用 EFGP 裡設定的簽核層級名稱查詢。

如果環境中的簽核層級名稱就是 `Lv1`：

```text
pLevelName = Lv1
pApprovalLevelType = FunctionLevel
pIsDeptManager = false
```

如果實際名稱是 `一級主管`、`課長級`、`处长级` 等，`pLevelName` 必須改傳 EFGP 中的實際層級名稱。

URL：

```text
POST http://{EFGP_HOST}:{PORT}/{SITE_NAME}/services/WorkflowService
```

SOAP Method：

```text
findManagerByAppLvl
```

入參：

| 參數 | 型別 | 必填 | 說明 |
|---|---|---:|---|
| `pUserId` | string | Y | 查詢基準使用者帳號，通常是申請人 |
| `pOrgUnitOID` | string | Y | 使用者所在組織單位 OID，可由 `fetchOrgUnitOfUserId` 取得 |
| `pLevelName` | string | Y | 簽核層級名稱，例如 `Lv1`、`一級主管`、`处长级` |
| `pApprovalLevelType` | string | Y | Function Level 查詢通常傳 `FunctionLevel` |
| `pIsDeptManager` | boolean | Y | 查 Function Level 主管通常傳 `false`；若要查部門主管需依 EFGP 設定測試 `true` |

出參：

| 回傳節點 | 型別 | 說明 |
|---|---|---|
| `findManagerByAppLvlReturn` | string XML | 主管資訊 XML |

常用欄位：

| 欄位 | 說明 |
|---|---|
| `userId` | 主管帳號 |
| `userName` | 主管姓名，若回傳 XML 有提供 |
| `OID` | 主管使用者 OID，若回傳 XML 有提供 |

Request 範例：查 Lv1 manager

```xml
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:web="http://webservice.nana.dsc.com/"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <soapenv:Header/>
  <soapenv:Body>
    <web:findManagerByAppLvl soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
      <pUserId xsi:type="xsd:string">T0088</pUserId>
      <pOrgUnitOID xsi:type="xsd:string">ORG_UNIT_OID</pOrgUnitOID>
      <pLevelName xsi:type="xsd:string">Lv1</pLevelName>
      <pApprovalLevelType xsi:type="xsd:string">FunctionLevel</pApprovalLevelType>
      <pIsDeptManager xsi:type="xsd:boolean">false</pIsDeptManager>
    </web:findManagerByAppLvl>
  </soapenv:Body>
</soapenv:Envelope>
```

Response 核心內容範例：

```xml
<com.dsc.nana.data__transfer.UserForInvokingDTO>
  <OID>USER_OID</OID>
  <userId>T0100</userId>
  <userName>Manager</userName>
</com.dsc.nana.data__transfer.UserForInvokingDTO>
```

## 6. findFormOIDsOfProcess - 查流程套件的表單 OID

拋送表單 XML 前，需要知道流程套件使用的表單定義 OID。

URL：

```text
POST http://{EFGP_HOST}:{PORT}/{SITE_NAME}/services/WorkflowService
```

SOAP Method：

```text
findFormOIDsOfProcess
```

入參：

| 參數 | 型別 | 必填 | 說明 |
|---|---|---:|---|
| `pProcessPackageId` | string | Y | 流程套件 ID，例如 `Payment_Apply` |

出參：

| 回傳節點 | 型別 | 說明 |
|---|---|---|
| `findFormOIDsOfProcessReturn` | string | 表單 OID；可能有多筆，以逗號分隔 |

範例：

```text
0af5253cefaf100484d196fddccb88ca,bc09f160efb2100484d196fddccb88ca
```

若回傳多筆，需依流程版本判斷要用哪一筆。常見做法是取最後一筆作為目前表單 OID，但正式邏輯仍應以 EFGP 流程版本規則為準。

Request 範例：

```xml
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:web="http://webservice.nana.dsc.com/"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <soapenv:Header/>
  <soapenv:Body>
    <web:findFormOIDsOfProcess soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
      <pProcessPackageId xsi:type="xsd:string">Payment_Apply</pProcessPackageId>
    </web:findFormOIDsOfProcess>
  </soapenv:Body>
</soapenv:Envelope>
```

## 7. getFormFieldTemplate - 取得表單 XML Template

取得表單欄位結構後，呼叫端要把欄位值填入 XML，再交給 `invokeProcess`。

URL：

```text
POST http://{EFGP_HOST}:{PORT}/{SITE_NAME}/services/WorkflowService
```

SOAP Method：

```text
getFormFieldTemplate
```

入參：

| 參數 | 型別 | 必填 | 說明 |
|---|---|---:|---|
| `pFormDefinitionOID` | string | Y | 表單定義 OID，由 `findFormOIDsOfProcess` 取得 |

出參：

| 回傳節點 | 型別 | 說明 |
|---|---|---|
| `getFormFieldTemplateReturn` | string XML | 表單欄位 XML template |

Request 範例：

```xml
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:web="http://webservice.nana.dsc.com/"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <soapenv:Header/>
  <soapenv:Body>
    <web:getFormFieldTemplate soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
      <pFormDefinitionOID xsi:type="xsd:string">FORM_DEFINITION_OID</pFormDefinitionOID>
    </web:getFormFieldTemplate>
  </soapenv:Body>
</soapenv:Envelope>
```

Template 範例：

```xml
<sign_form>
  <form_date id="form_date" dataType="java.util.Date"></form_date>
  <subject id="subject" dataType="java.lang.String"></subject>
  <memo id="memo" dataType="java.lang.String"></memo>
  <send_employeeId id="send_employeeId" dataType="java.lang.String"></send_employeeId>
  <send_empDeptId id="send_empDeptId" dataType="java.lang.String"></send_empDeptId>
  <asignGrid id="asignGrid">
    <records>
      <record id="">
        <item id="empId" dataType="java.lang.String" perDataProId=""></item>
        <item id="empName" dataType="java.lang.String" perDataProId=""></item>
      </record>
    </records>
  </asignGrid>
</sign_form>
```

## 8. invokeProcess - 拋送流程單

這是最常用的標準拋單方法。

URL：

```text
POST http://{EFGP_HOST}:{PORT}/{SITE_NAME}/services/WorkflowService
```

SOAP Method：

```text
invokeProcess
```

### 版本 A：只拋流程，不帶表單 XML

入參：

| 參數 | 型別 | 必填 | 說明 |
|---|---|---:|---|
| `pProcessPackageId` | string | Y | 流程套件 ID |
| `pRequesterId` | string | Y | 申請人帳號 |
| `pOrgUnitId` | string | Y | 申請人組織單位 OID |
| `pSubject` | string | Y | 流程主旨 |

出參：

| 回傳節點 | 型別 | 說明 |
|---|---|---|
| `invokeProcessReturn` | string | 流程序號 |

### 版本 B：拋流程並帶表單 XML

此版本最常用。

入參：

| 參數 | 型別 | 必填 | 說明 |
|---|---|---:|---|
| `pProcessPackageId` | string | Y | 流程套件 ID，例如 `Payment_Apply` |
| `pRequesterId` | string | Y | 申請人帳號，例如 `T0088` |
| `pOrgUnitId` | string | Y | 申請人組織單位 OID |
| `pFormDefOID` | string | Y | 表單定義 OID |
| `pFormFieldValue` | string XML | Y | 已填值的表單 XML |
| `pSubject` | string | Y | 流程主旨 |

出參：

| 回傳節點 | 型別 | 說明 |
|---|---|---|
| `invokeProcessReturn` | string | EFGP 流程序號，例如 `Payment_Apply00000561` |

`pFormFieldValue` 是字串型態。若直接放在 SOAP XML 的文字節點，內容需要 XML escape。

原始表單 XML：

```xml
<sign_form>
  <form_date id="form_date" dataType="java.util.Date">2026/07/03</form_date>
  <subject id="subject" dataType="java.lang.String">GEN-2026070001</subject>
  <memo id="memo" dataType="java.lang.String">test invoke process</memo>
  <send_employeeId id="send_employeeId" dataType="java.lang.String">T0088</send_employeeId>
  <send_empDeptId id="send_empDeptId" dataType="java.lang.String">ORG_UNIT_OID</send_empDeptId>
  <managerUserId id="managerUserId" dataType="java.lang.String">T0100</managerUserId>
</sign_form>
```

放入 SOAP 時：

```xml
<pFormFieldValue xsi:type="xsd:string">&lt;sign_form&gt;...&lt;/sign_form&gt;</pFormFieldValue>
```

Request 範例：

```xml
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:web="http://webservice.nana.dsc.com/"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <soapenv:Header/>
  <soapenv:Body>
    <web:invokeProcess soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
      <pProcessPackageId xsi:type="xsd:string">Payment_Apply</pProcessPackageId>
      <pRequesterId xsi:type="xsd:string">T0088</pRequesterId>
      <pOrgUnitId xsi:type="xsd:string">ORG_UNIT_OID</pOrgUnitId>
      <pFormDefOID xsi:type="xsd:string">FORM_DEFINITION_OID</pFormDefOID>
      <pFormFieldValue xsi:type="xsd:string">&lt;sign_form&gt;&lt;form_date id=&quot;form_date&quot; dataType=&quot;java.util.Date&quot;&gt;2026/07/03&lt;/form_date&gt;&lt;subject id=&quot;subject&quot; dataType=&quot;java.lang.String&quot;&gt;GEN-2026070001&lt;/subject&gt;&lt;/sign_form&gt;</pFormFieldValue>
      <pSubject xsi:type="xsd:string">GEN-2026070001</pSubject>
    </web:invokeProcess>
  </soapenv:Body>
</soapenv:Envelope>
```

## 9. 其他拋單相關方法

### invokeProcessByOrg

多公司/多組織環境需要明確指定 `pOrgId` 時使用。

版本 A 不帶表單 XML：

| 參數 | 型別 | 必填 | 說明 |
|---|---|---:|---|
| `pProcessPackageId` | string | Y | 流程套件 ID |
| `pRequesterId` | string | Y | 申請人帳號 |
| `pOrgUnitId` | string | Y | 申請人組織單位 OID |
| `pOrgId` | string | Y | 公司/組織 ID |
| `pSubject` | string | Y | 流程主旨 |

版本 B 帶表單 XML：

| 參數 | 型別 | 必填 | 說明 |
|---|---|---:|---|
| `pProcessPackageId` | string | Y | 流程套件 ID |
| `pRequesterId` | string | Y | 申請人帳號 |
| `pOrgUnitId` | string | Y | 申請人組織單位 OID |
| `pOrgId` | string | Y | 公司/組織 ID |
| `pFormDefOID` | string | Y | 表單定義 OID |
| `pFormFieldValue` | string XML | Y | 已填值的表單 XML |
| `pSubject` | string | Y | 流程主旨 |

出參：`invokeProcessByOrgReturn`，型別 string，內容為 EFGP 流程序號。

### invokeProcessByParameter

流程設計使用啟動參數而不是表單 XML 時使用。

| 參數 | 型別 | 必填 | 說明 |
|---|---|---:|---|
| `pProcessPackageId` | string | Y | 流程套件 ID |
| `pRequesterId` | string | Y | 申請人帳號 |
| `pOrgUnitId` | string | Y | 申請人組織單位 OID |
| `pParameterId` | string | Y | 啟動參數 ID |
| `pInvokeParameter` | string | Y | 啟動參數值 |
| `pSubject` | string | Y | 流程主旨 |

出參：`invokeProcessByParameterReturn`，型別 string，內容為 EFGP 流程序號。

### invokeProcessByParameterByOrg

指定公司/組織，且使用啟動參數拋單。

| 參數 | 型別 | 必填 | 說明 |
|---|---|---:|---|
| `pProcessPackageId` | string | Y | 流程套件 ID |
| `pRequesterId` | string | Y | 申請人帳號 |
| `pOrgUnitId` | string | Y | 申請人組織單位 OID |
| `pOrgId` | string | Y | 公司/組織 ID |
| `pParameterId` | string | Y | 啟動參數 ID |
| `pInvokeParameter` | string | Y | 啟動參數值 |
| `pSubject` | string | Y | 流程主旨 |

出參：`invokeProcessByParameterByOrgReturn`，型別 string，內容為 EFGP 流程序號。

### invokeProcessAndAddCustAct

拋單時同時加入自訂關卡/加簽定義。

| 參數 | 型別 | 必填 | 說明 |
|---|---|---:|---|
| `pProcessPackageId` | string | Y | 流程套件 ID |
| `pRequesterId` | string | Y | 申請人帳號 |
| `pOrgUnitId` | string | Y | 申請人組織單位 OID |
| `pFormDefOID` | string | Y | 表單定義 OID |
| `pFormFieldValue` | string XML | Y | 已填值的表單 XML |
| `pSubject` | string | Y | 流程主旨 |
| `pPostPSActDefsAsXML` | string XML | Y | 自訂關卡/加簽定義 XML |

出參：`invokeProcessAndAddCustActReturn`，型別 string，內容為 EFGP 流程序號。

### invokeProcessAndAddCustActByOrg

指定公司/組織拋單，且同時加入自訂關卡/加簽定義。

| 參數 | 型別 | 必填 | 說明 |
|---|---|---:|---|
| `pProcessPackageId` | string | Y | 流程套件 ID |
| `pRequesterId` | string | Y | 申請人帳號 |
| `pOrgUnitId` | string | Y | 申請人組織單位 OID |
| `pOrgId` | string | Y | 公司/組織 ID |
| `pFormDefOID` | string | Y | 表單定義 OID |
| `pFormFieldValue` | string XML | Y | 已填值的表單 XML |
| `pSubject` | string | Y | 流程主旨 |
| `pPostPSActDefsAsXML` | string XML | Y | 自訂關卡/加簽定義 XML |

出參：`invokeProcessAndAddCustActByOrgReturn`，型別 string，內容為 EFGP 流程序號。

## 10. fetchProcInstanceWithSerialNo - 查流程狀態

拋單成功後會得到流程序號，可用此方法查詢流程狀態。

URL：

```text
POST http://{EFGP_HOST}:{PORT}/{SITE_NAME}/services/WorkflowService
```

SOAP Method：

```text
fetchProcInstanceWithSerialNo
```

入參：

| 參數 | 型別 | 必填 | 說明 |
|---|---|---:|---|
| `pProcessInstanceSerialNo` | string | Y | EFGP 流程序號 |

出參：

| 回傳節點 | 型別 | 說明 |
|---|---|---|
| `fetchProcInstanceWithSerialNoReturn` | string XML | 流程實例資訊 |

常用欄位：

| 欄位 | 說明 |
|---|---|
| `processId` | 流程 ID |
| `processName` | 流程名稱 |
| `createdTime` | 建立時間 |
| `requesterId` | 申請人帳號 |
| `requesterName` | 申請人姓名 |
| `state` | 流程狀態 |
| `OID` | 流程實例 OID |
| `serialNo` | 流程序號 |
| `subject` | 主旨 |
| `abortComment` | 取消/中止原因 |

常見狀態：

| `state` | 說明 |
|---|---|
| `open.running` | 流程進行中 |
| `closed.completed` | 流程完成 |
| `closed.aborted` | 流程取消 |
| `closed.terminated` | 流程中止 |

Request 範例：

```xml
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:web="http://webservice.nana.dsc.com/"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <soapenv:Header/>
  <soapenv:Body>
    <web:fetchProcInstanceWithSerialNo soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
      <pProcessInstanceSerialNo xsi:type="xsd:string">Payment_Apply00000561</pProcessInstanceSerialNo>
    </web:fetchProcInstanceWithSerialNo>
  </soapenv:Body>
</soapenv:Envelope>
```

Response 核心內容範例：

```xml
<com.dsc.nana.services.webservice.SimpleProcessInfo>
  <processId>Payment_Apply</processId>
  <processName>通用申請單</processName>
  <createdTime>2026-07-03 10:30:00.0</createdTime>
  <requesterId>T0088</requesterId>
  <requesterName>Frank</requesterName>
  <state>open.running</state>
  <OID>1482898cef7d100484d196fddccb88ca</OID>
  <serialNo>Payment_Apply00000561</serialNo>
  <subject>GEN-2026070001</subject>
  <abortComment></abortComment>
</com.dsc.nana.services.webservice.SimpleProcessInfo>
```

## 11. 拋單前必要資料對照

| 資料 | 取得方式 |
|---|---|
| `pProcessPackageId` | EFGP 流程套件 ID，由流程設計/部署資訊取得 |
| `pRequesterId` | 申請人帳號 |
| `pOrgUnitId` | 呼叫 `fetchOrgUnitOfUserId(pRequesterId)` 後取組織 `OID` |
| `pFormDefOID` | 呼叫 `findFormOIDsOfProcess(pProcessPackageId)` |
| `pFormFieldValue` | 呼叫 `getFormFieldTemplate(pFormDefOID)` 後填入表單值 |
| `pSubject` | 呼叫端自行組流程主旨 |
| Lv1 manager | 呼叫 `findManagerByAppLvl(pRequesterId, pOrgUnitOID, "Lv1", "FunctionLevel", false)`；其中 `"Lv1"` 要替換成 EFGP 實際層級名稱 |

## 12. 建議測試順序

1. 開 WSDL 確認服務可用。

```text
http://{EFGP_HOST}:{PORT}/{SITE_NAME}/services/WorkflowService?wsdl
```

2. 用 `fetchOrgUnitOfUserId` 查申請人組織 OID。
3. 用 `findManagerByAppLvl` 查 Lv1 manager，確認回傳有 `userId`。
4. 用 `findFormOIDsOfProcess` 查表單 OID。
5. 用 `getFormFieldTemplate` 查表單 XML template。
6. 填入表單 XML 後呼叫 `invokeProcess`。
7. 用 `fetchProcInstanceWithSerialNo` 查流程狀態。

## 13. TIPTOP 觸發 EFGP 拋單：TipTopIntegration

TIPTOP 整合型拋單不是呼叫 `WorkflowService.invokeProcess`，而是呼叫 NaNaWeb 另一個 service：

```text
http://{EFGP_HOST}:{PORT}/{SITE_NAME}/services/TipTopIntegration
```

範例：

```text
http://127.0.0.1:8080/NaNaWeb/services/TipTopIntegration
http://192.168.10.19:8086/NaNaWeb/services/TipTopIntegration
```

WSDL URL：

```text
http://{EFGP_HOST}:{PORT}/{SITE_NAME}/services/TipTopIntegration?wsdl
```

專案裡 dsc.jar 的 stub 預設值曾出現：

```text
http://192.168.36.81:8086/NaNaWeb/services/TipTopIntegration
```

目前設定檔使用：

```text
http://192.168.10.19:8086/NaNaWeb/services/TipTopIntegration
```

SOAP 共通設定：

```text
SOAP Version : SOAP 1.1
Style        : RPC
Use          : encoded
Namespace    : http://webservice.nana.dsc.com/
SOAPAction   : 空字串
```

### 13.1 easyFlowGPGateWay - TIPTOP XML 觸發 EFGP 建單

這是 TIPTOP 整合拋單的主要入口。

SOAP Method 在 stub 裡顯示為：

```text
EasyFlowGPGateWay
```

Java interface method 名稱為：

```text
easyFlowGPGateWay
```

入參：

| 參數 | 型別 | 必填 | 說明 |
|---|---|---:|---|
| `pRequestXML` | string XML | Y | TIPTOP/EFGP 整合格式的 request XML |

出參：

| 回傳節點 | 型別 | 說明 |
|---|---|---|
| `EasyFlowGPGateWayReturn` | string XML | EFGP gateway response XML |

Request XML 最外層格式：

```xml
<Request>
  <RequestMethod>CreateForm</RequestMethod>
  <LogOnInfo>
    <SenderIP>192.168.36.51</SenderIP>
    <ReceiverIP>192.168.36.80</ReceiverIP>
    <EFSiteName>NaNaWeb</EFSiteName>
    <EFLogonID>T1945</EFLogonID>
    <OrgUnitID>1311005-A</OrgUnitID>
    <TPServerIP>192.168.36.76</TPServerIP>
    <TPServerEnv>topprod</TPServerEnv>
  </LogOnInfo>
  <RequestContent>
    <Form>
      <Language>Big5</Language>
      <PlantID>GTAM</PlantID>
      <ProgramID>aimi150</ProgramID>
      <SourceFormID>AIZ1</SourceFormID>
      <SourceFormNum>AIZ1-23020246</SourceFormNum>
      <FormCreatorID>T1945</FormCreatorID>
      <FormOwnerID>T1945</FormOwnerID>
      <ContentText>
        <title>TIPTOP Plant ID:GTAM PDM 料號申請 AIZ1-23020246</title>
        <head>
          <imaa00 type="0">I</imaa00>
          <imaano type="0">AIZ1-23020246</imaano>
          <imaa01 type="0">P1-3139687-AA0</imaa01>
          <imaa02 type="0">STEEL SPACER</imaa02>
          <imaauser type="0">T1945</imaauser>
          <imaagrup type="0">1311005-A</imaagrup>
        </head>
        <body></body>
        <memo></memo>
      </ContentText>
    </Form>
  </RequestContent>
</Request>
```

`LogOnInfo` 欄位：

| 欄位 | 說明 |
|---|---|
| `SenderIP` | 呼叫來源 IP，通常是 TIPTOP 或中介主機 |
| `ReceiverIP` | EFGP / NaNaWeb 主機 IP |
| `EFSiteName` | EFGP site name，例如 `NaNaWeb` |
| `EFLogonID` | EFGP 登入/申請人帳號 |
| `OrgUnitID` | 申請人部門/組織代號；此格式與 `WorkflowService.invokeProcess` 的 OID 欄位不同 |
| `TPServerIP` | TIPTOP server IP |
| `TPServerEnv` | TIPTOP 環境，例如 `topprod`、`toptest` |

`RequestContent/Form` 欄位：

| 欄位 | 說明 |
|---|---|
| `Language` | 語系，例如 `Big5` |
| `PlantID` | TIPTOP 廠別 |
| `ProgramID` | TIPTOP 作業代號，例如 `aimi150` |
| `SourceFormID` | 來源單別 |
| `SourceFormNum` | 來源單號 |
| `FormCreatorID` | 建單人 |
| `FormOwnerID` | 單據擁有人 |
| `ContentText/title` | 流程/表單標題 |
| `ContentText/head` | 表頭欄位資料，欄位名稱通常對應 TIPTOP 欄位 |
| `ContentText/body` | 表身資料 |
| `ContentText/memo` | 備註 |

SOAP Request 範例：

```xml
<soapenv:Envelope
    xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    xmlns:web="http://webservice.nana.dsc.com/"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <soapenv:Header/>
  <soapenv:Body>
    <web:EasyFlowGPGateWay soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
      <pRequestXML xsi:type="xsd:string">&lt;Request&gt;...&lt;/Request&gt;</pRequestXML>
    </web:EasyFlowGPGateWay>
  </soapenv:Body>
</soapenv:Envelope>
```

Response XML 範例：

```xml
<Response>
  <ResponseType>CreateForm</ResponseType>
  <ResponseInfo>
    <SenderIP>192.168.36.80</SenderIP>
    <ReceiverIP>192.168.36.51</ReceiverIP>
  </ResponseInfo>
  <ResponseContent>
    <ReturnInfo>
      <ReturnStatus>Y</ReturnStatus>
      <ReturnDescribe>NO ERROR</ReturnDescribe>
    </ReturnInfo>
    <ContentText>
      <Form>
        <StatSlip/>
        <PlantID>GTAM</PlantID>
        <ProgramID>aimi150</ProgramID>
        <SourceFormID>AIZ1</SourceFormID>
        <SourceFormNum>AIZ1-23020246</SourceFormNum>
        <Date>23/02/16</Date>
        <Time>16:17:15</Time>
        <Status>1</Status>
        <FormCreatorID>T1945</FormCreatorID>
        <FormOwnerID>T1945</FormOwnerID>
        <TargetFormID/>
        <TargetSheetNo>7693b390efd11004814a1bf89323dac1</TargetSheetNo>
        <ProcessSNo>TIPTOPPROCESSPKG_aimi15000034719</ProcessSNo>
        <Description>TIPTOP Plant ID:GTAM PDM 料號申請 AIZ1-23020246</Description>
        <ActionStatus>Y</ActionStatus>
        <ActionDescribe>Success</ActionDescribe>
        <TPServerIP>192.168.36.76</TPServerIP>
        <TPServerEnv>topprod</TPServerEnv>
        <SourceFormKey3/>
        <SourceFormKey4/>
        <SourceFormKey5/>
      </Form>
    </ContentText>
  </ResponseContent>
</Response>
```

成功判斷常用欄位：

| 欄位 | 成功值 | 說明 |
|---|---|---|
| `ReturnStatus` | `Y` | Gateway 呼叫結果 |
| `ActionStatus` | `Y` | 建單動作結果 |
| `ActionDescribe` | `Success` | 動作描述 |
| `ProcessSNo` | 非空 | EFGP 流程序號 |
| `TargetSheetNo` | 非空 | EFGP 目標表單/單據識別 |

### 13.2 TipTopIntegration 其他方法

`TipTopIntegration` service 另有以下方法：

| SOAP Method | 入參 | 出參 | 用途 |
|---|---|---|---|
| `checkTiptopBizLogic` | `pTiptopSOAP`, `pTiptopMethodName`, `pInputElementsString` | `checkTiptopBizLogicReturn` string | 檢查 TIPTOP business logic |
| `invokeTiptopCreateForm` | `pTiptopSOAP`, `pProcessInstanceSerialNumber`, `pFormID` | `invokeTiptopCreateFormReturn` string | 以 EFGP 流程序號/表單 ID 回觸 TIPTOP 建立表單或更新關聯 |
| `testTipTopWebService` | `pTiptopSoapAddress` | `testTipTopWebServiceReturn` string | 測試 TIPTOP WebService 可用性 |
| `getTiptopPlantID` | `pTiptopSOAP`, `pTiptopProgramID` | `getTiptopPlantIDReturn` string | 查 TIPTOP program 對應廠別 |

## 14. TIPTOPServiceGateWay - TIPTOP 端 WebService

這是 TIPTOP 自己提供的 WebService endpoint，不是 EFGP endpoint。EFGP 的 `TipTopIntegration` 可能會用它回查或回寫 TIPTOP。

URL 格式依 TIPTOP 環境而定。專案設定範例：

```text
http://192.168.36.76/web/ws/r/aws_ttsrv2_toptest?wsdl
```

stub 內 endpoint 不含 `?wsdl`：

```text
http://192.168.36.76/web/ws/r/aws_ttsrv2_toptest
```

Namespace：

```text
http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay
```

通用方法：

| Method | 入參 | 出參 | 說明 |
|---|---|---|---|
| `invokeSrv` | string | string | 呼叫 TIPTOP service 的通用入口 |
| `callbackSrv` | string | string | callback TIPTOP 的通用入口 |
| `syncProd` | string | string | 同步產品/料號類資料 |
| `invokeMdm` | string | string | 呼叫 MDM 整合入口 |

在「TIPTOP 觸發 EFGP 拋單」情境，通常外部呼叫的是 NaNaWeb 的 `TipTopIntegration.easyFlowGPGateWay`；`TIPTOPServiceGateWay` 是另一個方向，供 EFGP 或中介端去呼叫 TIPTOP。
