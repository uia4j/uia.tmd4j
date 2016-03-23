# 範例說明

假定系統中有兩個表格，紀錄員工與出勤紀錄。結構與關連如下：
### table: employee
* id - 主鍵。

### table: employee_manhour
* id - 主鍵。
* employee - 外鍵，關聯至 employee.id。

當員工退休時，系統要將其資料從資料庫 "local1" 搬移到 "local2"。

# 設計

## XML

* 定義 "Retire" Executor，執行工作 "job1"，"job1" 會將 employee 的資料從 local1 搬移到 local2。
```
<executorSpace>
    <executor name="Retire" source="local1" target="local2" task="job1" />
</executorSpace>
```

* 定義 "job1" 處理 employee 表格，並設定 "job2" 緊接在 "job1" 後。"job1" 和 "job2" 間的關聯是 employee.id=employee_manhour.employee。
```
<task name="job1">
    <sourceSelect table="employee" />
    <targetUpdate />
    <nexts>
        <plan name="job2">
            <join>
                <column source="id">employee</column>
            </join>
        </plan>
    </nexts>
</task>
```

* 定義 "job2" 處理 employee_manhour 表格。
```
<task name="job2">
    <sourceSelect table="employee_manhour" />
    <targetUpdate />
</task>
```

* 定義 "local1" & "local2" 資料庫。
```
<dbServer>
    <id>local1</id>
    <host>localhost</host>
    <port>1433</port>
    <dbName>db1</dbName>
    <user>user1</user>
    <password>1234</password>
    <dbType>MSSQL</dbType>
</dbServer>
<dbServer>
    <id>local2</id>
    <host>localhost</host>
    <port>1433</port>
    <dbName>db2</dbName>
    <user>user2</user>
    <password>1234</password>
    <dbType>MSSQL</dbType>
</dbServer>
```

完整的 XML:

```

<?xml version="1.0" encoding="UTF-8"?>
<tmd>
	<executorSpace>
		<executor name="Retire" source="local1" target="local2" task="job1" />
	</executorSpace>
	<taskSpace>
        <task name="job1">
            <sourceSelect table="employee" />
            <targetUpdate />
            <nexts>
                <plan name="job2">
                    <join>
                        <column source="id">employee</column>
                    </join>
                </plan>
            </nexts>
        </task>
        <task name="job2">
            <sourceSelect table="employee_manhour" />
            <targetUpdate />
        </task>
    </taskSpace>
    <tableSpace />
    <databaseSpace>
        <dbServer>
            <id>local1</id>
            <host>localhost</host>
            <port>1433</port>
            <dbName>db1</dbName>
            <user>user1</user>
            <password>1234</password>
            <dbType>MSSQL</dbType>
        </dbServer>
        <dbServer>
            <id>local2</id>
            <host>localhost</host>
            <port>1433</port>
            <dbName>db2</dbName>
            <user>user2</user>
            <password>1234</password>
            <dbType>MSSQL</dbType>
        </dbServer>
    </databaseSpace>
</tmd>
```

## java code
當一個編號為 0098712 的員工退休時，系統建立一個 "Retire" 執行個體來完成上述的工作。
Retire 會執行 "job1"，而 "job1" 處理的表格 employee 主鍵為 id，所以執行條件為 id="0098712"。
```
TaskFactory factory = new TaskFactory(new File("sample.xml"));
TaskExecutor executor = factory.createExecutor("Retire");

TreeMap<String, Object> where = new TreeMap<String, Object>();
where.put("ID", "0098712");

executor.run(where);
```