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

@app.route("/goals/progress/<int:studentno>", methods=['GET'])
def ret_goal_progress(studentno):

    # Build SQL requests for total and compeleted goals
    totalsql = "select * from Goals where [Student_ID] = %i" % (studentno)
    complsql = "select * from Goals where [Student_ID] = %i AND [Goal_Status] = 'Completed'" % (studentno)
    # Execute sql
    total = pandas.read_sql(totalsql,cnxn)
    compl = pandas.read_sql(complsql,cnxn)
    # Get number total and completed goals
    totalnum = len(total.index)
    complnum = len(compl.index)
    # Put into a list for return
    returnvar = [complnum, totalnum]
    # Cannot return ints
    # Returns in the form [number of completed goals, total number of goals]
    return str(returnvar)

@app.route("/goals/<int:studentno>", methods=['GET'])
def ret_goals(studentno):
    sql = "select * from Goals where [Student_ID] = %i" % (studentno)
    df = pandas.read_sql(sql,cnxn)
    data = df.to_json()
    return data

@app.route("/goals/<int:goalid>", methods=['DELETE'])
def delete_goal(goalid):
    print('something')
    sql = "DELETE FROM Goals WHERE [Goal_ID] = %i" % (goalid)
    cursor.execute(sql)
    return "Success"


@app.route("/goals", methods=['POST'])
def create_goal():

    # Find the highest goal_ID number
    maxidsql = "select MAX(Goal_ID) FROM Goals"
    maxid = pandas.read_sql(maxidsql,cnxn)

    maxid = maxid['Expr1000']
    print(maxid)

    Goal_ID=maxid+1
    Student_ID=request.json.get('Student_ID', "")
    Goal_Status=request.json.get('Goal_Status', "")
    Goal_Type=request.json.get('Goal_Type', "")
    Exp_Date=request.json.get('Exp_Date', "")
    Description=request.json.get('Description', "")


    sql = "INSERT INTO Goals (Goal_ID, Student_ID, Goal_Status, Goal_Type, Exp_Date, Description)" \
          " VALUES (%i, %i, '%s', '%s', '%s', '%s');" % (Goal_ID, Student_ID['0'], Goal_Status['0'], Goal_Type['0'], Exp_Date['0'], Description['0'])

    print(sql)

    cursor.execute(sql)
    return "Success"

@app.route("/goals", methods=['PUT'])
def update_goal():

    Goal_ID=request.json.get('Goal_ID', "")
    Student_ID=request.json.get('Student_ID', "")
    Goal_Status=request.json.get('Goal_Status', "")
    Goal_Type=request.json.get('Goal_Type', "")
    Exp_Date=request.json.get('Exp_Date', "")
    Description=request.json.get('Description', "")

    sql = "UPDATE Goals SET Goal_Status = '%s', Goal_Type = '%s', Exp_Date = '%s', Description = '%s' WHERE Goal_ID = %i;" % (Goal_Status['0'], Goal_Type['0'], Exp_Date['0'], Description['0'], Goal_ID['0'])

    print(sql)

    cursor.execute(sql)
    return "Success"

@app.route("/")
def hello():
    return "Hello World! Run from Blake's Computer at 121.208.245.165 port 5000. Server init at:  " + str(time)

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000)

