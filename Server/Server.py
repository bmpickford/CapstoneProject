from flask import Flask
import datetime
import sqlalchemy
import pyodbc
import pandas
import json
from flask import jsonify
from flask import request

app = Flask(__name__)

time = datetime.datetime.now().time()

#-------------- DB Connection
connStr = (
    r"DRIVER={Microsoft Access Driver (*.mdb, *.accdb)};"
    r"DBQ=C:\School\Uni\Year 4 Sem 1\IFB398\Database\AppDB.mdb;"
)
cnxn = pyodbc.connect(connStr)

cursor = cnxn.cursor()
#------------------------------



@app.route("/goals")
def allgoals():
    df = pandas.read_sql("select * from Goals", cnxn)
    data = df.to_json()
    return data

@app.route("/goals/present/<int:studentno>", methods=['GET'])
def ret_pres_goals(studentno):
    sql = "select * from Goals where [Student_ID] = %i AND [Goal_Status] = 'Active'" % (studentno)
    df = pandas.read_sql(sql,cnxn)
    data = df.to_json()
    return data

@app.route("/goals/past/<int:studentno>", methods=['GET'])
def ret_past_goals(studentno):
    sql = "select * from Goals where [Student_ID] = %i AND [Goal_Status] = 'Expired'" % (studentno)
    df = pandas.read_sql(sql, cnxn)
    data = df.to_json()
    return data

@app.route("/goals/<int:studentno>", methods=['GET'])
def ret_goals(studentno):
    sql = "select * from Goals where [Student_ID] = %i" % (studentno)
    df = pandas.read_sql(sql,cnxn)
    data = df.to_json()
    return data

@app.route("/goals", methods=['POST'])
def create_goal():




    Goal_ID=9
    Student_ID=request.json.get('Student_ID', "")
    Goal_Status=request.json.get('Goal_Status', "")
    Goal_Type=request.json.get('Goal_Type', "")
    Exp_Date=request.json.get('Exp_Date', "")
    Description=request.json.get('Description', "")


    #newdata.to_sql("Goals", cnxn)

    sql = "INSERT INTO Goals (Goal_ID, Student_ID, Goal_Status, Goal_Type, Exp_Date, Description)" \
          " VALUES (%i, %i, '%s', '%s', '%s', '%s');" % (Goal_ID, Student_ID['0'], Goal_Status['0'], Goal_Type['0'], Exp_Date['0'], Description['0'])


    print(sql)

    cursor.execute(sql)

    #df.to_sql("Goals", cnxn)
    #print(request.json)

    #print(data)
    #print(newdata)
    return "it got to here i guess"





@app.route("/goals/<int:goalid>")

@app.route("/")
def hello():
    return "Hello World! Run from Blake's Computer at 121.208.245.165 port 5000. Server init at:  " + str(time)

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000)

