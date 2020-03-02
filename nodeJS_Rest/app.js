const express = require('express')
// Import the axios library, to make HTTP requests
const axios = require('axios')
var MongoClient = require('mongodb').MongoClient;
var bodyParser = require('body-parser')
const url = require('url');
const execSync = require('child_process').execSync;
const moment = require('moment');
const config = require('./config/config.js')
const fs = require('fs');
const app = express()
const cookieParser = require("cookie-parser");

app.use(cookieParser())
app.use(bodyParser.json({ type: 'application/json' }))

const clientID = `${global.gConfig.clientID}`;
const clientSecret = `${global.gConfig.clientSecret}`;

var dburl = "mongodb://localhost:27017/";

const lineReader = require('line-reader');

var accessToken;
// Declare the redirect route
app.get('/oauth/redirect', (req, res) => {
  // The req.query object has the query params that
  // were sent to this route. We want the `code` param
  console.log(req.query);
  const requestToken = req.query.code
  axios({
    // make a POST request
    method: 'post',
    // to the Github authentication API, with the client ID, client secret
    // and request token
    url: `https://github.com/login/oauth/access_token?client_id=${clientID}&client_secret=${clientSecret}&code=${requestToken}`,
    // Set the content type header, so that we get the response in JSOn
    headers: {
         accept: 'application/json'
    }
  }).then((response) => {
    // Once we get the response, extract the access token from
    // the response body
    console.log(response.data);
    accessToken= response.data.access_token;
    //const username = response.data.username
    // redirect the user to the welcome page, along with the access token
    res.redirect(`/welcome.html?access_token=${accessToken}`)
  })
})

app.use(express.static(__dirname + '/frontend'))

app.post('/api/clone', function(req, res) {

  execSync('git clone '+`${req.body.github} ./temp`, (error, stdout, stderr) => {
    if (error) {
        console.error(`exec error: ${error}`);
        return;
      }
      console.log(`stdout: ${stdout}`);
      // console.error(`stderr: ${stderr}`);

      // <script>document.getElementById('toggle').click();</script>
    })
    execSync('java -jar simian-2.5.10.jar -reportDuplicateText ./temp/**.java > .\\output.txt | type .\\output.txt', (error, stdout, stderr) => {
      if (error) {
          console.error(`exec error: ${error}`);
        }
        console.log(`stdout: ${stdout}`);
        // console.error(`stderr: ${stderr}`);
        // console.log(objCheck);
      })
      execSync('rmdir /s /q .\\temp', (error, stdout, stderr) => {
        if (error) {
            console.error(`exec error: ${error}`);
          }
          console.log('successfully remove temp folder');
          // console.error(`stderr: ${stderr}`);
          // console.log(objCheck);
        })
        var user = `${req.body.user}`;
        var repoName = `${req.body.reposName}`;
        let time = moment().format('MMMM Do YYYY, H:mm:ss');
        let data = fs.readFileSync('./output.txt','utf8');
        let obj = {owner: user, repoName: repoName, result: data, date: time};

        MongoClient.connect(dburl, {
        useUnifiedTopology: true,
        useNewUrlParser: true,
        }, function(err, db) {
          if (err) throw err;
          var dbo = db.db("Data");
            dbo.collection("analysedClone").insertOne(obj, function(err, res) {
              if (err) throw err;
              console.log("document inserted");
              db.close();
        });
        });
        res.json(obj);
})

app.post('/api/guestclone', function(req, res) {

  execSync('git clone '+`${req.body.github} ./temp`, (error, stdout, stderr) => {
    if (error) {
        console.error(`exec error: ${error}`);
        return;
      }
      console.log(`stdout: ${stdout}`);
      // console.error(`stderr: ${stderr}`);

      // <script>document.getElementById('toggle').click();</script>
    })
    execSync('java -jar simian-2.5.10.jar -reportDuplicateText ./temp/*.java > .\\output.txt | type .\\output.txt', (error, stdout, stderr) => {
      if (error) {
          console.error(`exec error: ${error}`);
        }
        console.log(`stdout: ${stdout}`);
        // console.error(`stderr: ${stderr}`);
        // console.log(objCheck);
      })
      execSync('rmdir /s /q .\\temp', (error, stdout, stderr) => {
        if (error) {
            console.error(`exec error: ${error}`);
          }
          console.log('successfully remove temp folder');
          // console.error(`stderr: ${stderr}`);
          // console.log(objCheck);
        })
        let data = fs.readFileSync('./output.txt','utf8');
        let obj = {result: data};
        res.json(obj);
})

app.get('/api/getResult', function(req, res){
  MongoClient.connect(dburl, {
  useUnifiedTopology: true,
  useNewUrlParser: true,
  }, function(err, db) {
    if (err) throw err;
    var dbo = db.db("Data");
    //Sort the result by name:
    var sort = { date: -1 };
    dbo.collection("analysedClone").find().sort(sort).limit(1).toArray(function(err, result) {
      if (err) throw err;
        res.json(result);
      console.log(result);
      db.close();
    });
  });
 });

 app.get('/history', function(req, res){
   res.redirect(`http://localhost:8001/history.html?access_token=${req.query.accessToken}`);
 })

app.post('/getHistory', function(req, res){
  MongoClient.connect(dburl, {
  useUnifiedTopology: true,
  useNewUrlParser: true,
  }, function(err, db) {
    if (err) throw err;
    var dbo = db.db("Data");
    //Sort the result by name:
  var quary = {owner: req.body.owner};
    var sort = { date: -1 };
    dbo.collection("analysedClone").find().sort(sort).toArray(function(err, result) {
      if (err) throw err;
        res.json(result);
      console.log(result);
      db.close();
    });
  });
})
// app.delete('/api/logout', function(req, res){
//   res.redirect('/');
// });
//localhost 8001
app.listen(global.gConfig.node_port,() => {
  console.log(`${global.gConfig.app_name} listening on port ${global.gConfig.node_port}`);
});
