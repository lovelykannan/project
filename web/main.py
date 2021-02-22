from flask import Flask,render_template,request,redirect,url_for
from flask_mail import Mail,Message
import pyrebase
import time
import datetime
from firebase_admin import db
from google.cloud import firestore
app=Flask(__name__)
config = {
    "apiKey": "AIzaSyBxCC3OhktkEoAo74iv4CzaQsPKHYq8Ejg",
    "authDomain": "infra-457da.firebaseapp.com",
    "databaseURL": "https://infra-457da-default-rtdb.firebaseio.com",
    "projectId": "infra-457da",
    "storageBucket": "infra-457da.appspot.com",
    "messagingSenderId": "51695903936",
    "appId": "1:51695903936:web:fda117bb4f747b0264b925",
    "measurementId": "G-RP7QX1PZQ7"
}
firebase = pyrebase.initialize_app(config)
auth = firebase.auth()
db = firebase.database()
LIFE = db.child("users").get()
to=[]
for i in LIFE:
    to.append(i.val())


@app.route('/',methods=['GET','POST'])
def login():
   failed='check your Email/Password'
   if request.method=='POST':
       email=request.form['email']
       password=request.form['password']
       try:
          auth.sign_in_with_email_and_password(email,password)
          return render_template('page1.html', t=to)

       except:
           return render_template('loginform.html',f=failed)
   return render_template("loginform.html")
@app.route('/logout')
def log():
    return render_template("page2.html")

if __name__=='__main__':
    app.run(debug=True)

