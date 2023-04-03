# EzFlowMan - 一個整合異質拋送表單給EasyFlow GP 的系統
拋送發單給 EasyFlowGP 5.x 排程工具 

說明:<br>
當使用鼎新 EasyFlow GP 時, 如果有些異質系統, 想要送簽核給 EasyFlow GP 時, 首先要在 EasyFlowGP上把表單於流程設計開發完成後, 再從異質系統處理介接拋送, 與取回簽核狀態.
這樣異質系統才能完成介接 EasyFlowGP 的部份, 但是如果多個異質系統都要介結時, 就顯得麻煩了. 
所以這個系統 EzFlowMan 為此而生, 讓異質系統拋送表單給EasyFlow GP簡單化, 透過配置即可.


使用:<br>
1. 首先須先完成在 EasyFlow GP 上的表單與流程
<img src="https://raw.githubusercontent.com/billchen198318/ezflowman/main/doc/pic/001.png"/><br/>

<br>

2. 配置EzFlowMan 設定<br>
要拋送的表單資料, 首先要在被拋送的業務邏輯資料主表中, 多增加4個欄位, 這四個欄位名稱沒有固定規範,可以自己定義, 以下範例如下圖
<img src="https://raw.githubusercontent.com/billchen198318/ezflowman/main/doc/pic/003.png"/><br/>
	 * '1' = open.running 		開單執行中
	 * '3' = closed.completed  	流程完成結束
	 * '4' = closed.aborted 		取消流程
	 * '5' = closed.terminated 	中止流程
	 * '0' = 待EzFlowMan送單給 EFGP 的資料
	 * '9' = EzFlowMan 與 EFGP 介接處理中發生異常錯誤   
   
定義確定完業務邏輯資料表後, 進入EzFlowMan 進行配置, 配置內容就是流程表單的欄位與DB 資料 Table欄位的對應, 也支持表單有Grid, 配置主Table與明細Table配置即可 , 如下圖
<img src="https://raw.githubusercontent.com/billchen198318/ezflowman/main/doc/pic/004.png"/><br/>
<br/>
拋送出給 EFGP 的表單範例內容<br/>
<img src="https://raw.githubusercontent.com/billchen198318/ezflowman/main/doc/pic/002.png"/><br/>

<br><br>
EzFlowMan 系統會依照配置對應的流程/業務邏輯Table 進行表單拋送，並依配置的Cron expression時間進行排程處理，再將拋送結果與之後後續的簽核結果，依照配置更新至業務邏輯表定義的欄位中，
異質系統直接用自己DB的業務表判定該欄位, 即可知道簽核結果.
