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
var ObjectID = require('mongodb').ObjectID;
var Git = require('nodegit');

app.use(cookieParser())
app.use(bodyParser.json({ type: 'application/json' }))

const clientID = `${global.gConfig.clientID}`;
const clientSecret = `${global.gConfig.clientSecret}`;

var dburl = `${global.gConfig.mongoUrl}`;

const lineReader = require('line-reader');


// Declare the redirect route
app.get('/oauth/redirect', (req, res) => {
  // The req.query object has the query params that
  // were sent to this route. We want the `code` param
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
    accessToken= response.data.access_token;
    res.cookie("access_token", `${response.data.access_token}`, { maxAge: 900000 });
    //const username = response.data.username
    // redirect the user to the welcome page, along with the access token
    res.redirect(`/welcome.html?access_token=${accessToken}`)
  })
})

app.use(express.static(__dirname + '/frontend'))

app.post('/api/clone', function(req, res) {

  const args = [
    "clone",
    `${req.body.github}`,
    `./${req.body.reposName}`
  ];

  const child = require("child_process").spawnSync("git", args);
console.log("clone");

let rawdata = fs.readFileSync('setting-config.json');
let setting = JSON.parse(rawdata);

var ObjectId = new ObjectID();
        var user = `${req.body.user}`;
        var repoName = `${req.body.reposName}`;
        let time = moment().format('MMMM Do YYYY, H:mm:ss');
        let obj = { _id: ObjectId, owner: user, repoName: repoName, date: time};
        execSync('java -jar MerryEngine.jar -DBport 27017 -DBurl localhost -sem '+setting.sem+' -syn '+setting.syn+' -model '+setting.model+' -size-filter '+setting.size+' -execID '+ ObjectId +' -c2vpath C:\\Users\\User\\Documents\\SP2019-DNC\\nodeJS_Rest\\code2vec -workingdir C:\\Users\\User\\Documents\\SP2019-DNC\\nodeJS_Rest\\dumpFolder -input C:\\Users\\User\\Documents\\SP2019-DNC\\nodeJS_Rest\\'+repoName+' -output C:\\Users\\User\\Documents\\SP2019-DNC\\nodeJS_Rest\\output.csv')
        console.log("Done getting results");

        MongoClient.connect(dburl, {
        useNewUrlParser: true,
        useUnifiedTopology: true
        }, function(err, db) {
          if (err) throw err;
          var dbo = db.db("MerryDB");
            dbo.collection("analysedRepoInfo").insertOne(obj, function(err, res) {
              if (err) throw err;
              console.log("document inserted");
        db.close();
        });
        });

        execSync(`rmdir /s /q .\\${req.body.reposName}`)
        console.log("remove folder");

        let defaultSetting = {"sem":"on","syn":"on","size":"on","model":"smo"};

        let data = JSON.stringify(defaultSetting);
        fs.writeFileSync('setting-config.json', data, (err) => {
            if (err) throw err;
            console.log('Data written to file');
        });
res.json(obj);
})

//This is API for analyze clone for non-github users
app.post('/api/guestclone', function(req, res) {
//clone from github function
const args = [
  "clone",
  `${req.body.github}`,
  "./temp"
];

const child = require("child_process").spawnSync("git", args);
console.log("clone");
var ObjectId = "temp";
      let time = moment().format('MMMM Do YYYY, H:mm:ss');
      let obj = { _id: ObjectId, date: time};
      execSync('java -jar MerryEngine.jar -DBport 27017 -DBurl localhost -execID '+ ObjectId +' -c2vpath C:\\Users\\User\\Documents\\SP2019-DNC\\nodeJS_Rest\\code2vec -workingdir C:\\Users\\User\\Documents\\SP2019-DNC\\nodeJS_Rest\\dumpFolder -input C:\\Users\\User\\Documents\\SP2019-DNC\\nodeJS_Rest\\temp -output C:\\Users\\User\\Documents\\SP2019-DNC\\nodeJS_Rest\\output.csv')
      console.log("Done getting results");
      MongoClient.connect(dburl, {
      useNewUrlParser: true,
      useUnifiedTopology: true
      }, function(err, db) {
        if (err) throw err;
        var dbo = db.db("MerryDB");
          dbo.collection("guestAnalsysed").insertOne(obj, function(err, res) {
            if (err) throw err;
            console.log("document inserted");
      db.close();
      });
      });

            execSync('rmdir /s /q .\\temp')
            console.log("remove folder");

res.json(obj);
})

app.get('/api/getGuestResult', function(req, res){
  MongoClient.connect(dburl, {
  useUnifiedTopology: true,
  useNewUrlParser: true,
  }, function(err, db) {
    if (err) throw err;
    var dbo = db.db("MerryDB");
    //Sort the result by latest date:
    dbo.collection('guestAnalsysed').aggregate([
       { $sort : { _id : -1 } },
       { $limit: 1 },
      {
    $project: {
      _id: {
        $toString: "$_id"
      }
    }
  },
    {
      $lookup:
       {
         from: 'Result',
         localField: '_id',
         foreignField: 'ExecutionID',
         as: 'cloneResult'
       }
     }
   ]).toArray(function(err, result) {
    if (err) throw err;
    console.log(JSON.stringify(result));
    res.json(result)
    db.close();
  });
  });
 });

 app.get('/api/getLatestResult', function(req, res){
   MongoClient.connect(dburl, {
   useUnifiedTopology: true,
   useNewUrlParser: true,
   }, function(err, db) {
     if (err) throw err;
     var dbo = db.db("MerryDB");
     //Sort the result by latest date:
     dbo.collection('analysedRepoInfo').aggregate([
        { $sort : { _id : -1 } },
        { $limit: 1 },
       {
     $project: {
       _id: {
         $toString: "$_id"
       }
     }
   },
     {
       $lookup:
        {
          from: 'Result',
          localField: '_id',
          foreignField: 'ExecutionID',
          as: 'cloneResult'
        }
      },
        {
          $unwind: "$cloneResult"
        },
        {
        $lookup:
         {
           from: 'graphInfo',
           localField: '_id',
           foreignField: '_id',
           as: 'cloneCount'
         }
      }
    ]).toArray(function(err, result) {
     if (err) throw err;
     console.log(result);
     res.json(result);
     db.close();
   });
   });
  });

 app.post('/api/getResultById', function(req, res){
   MongoClient.connect(dburl, {
   useUnifiedTopology: true,
   useNewUrlParser: true,
   }, function(err, db) {
  if (err) throw err;
  var dbo = db.db("MerryDB");
  dbo.collection('Result').aggregate([
     { $match: { ExecutionID: `${req.body.id}` }
},
     {
     $lookup:
      {
        from: 'graphInfo',
        localField: 'ExecutionID',
        foreignField: '_id',
        as: 'cloneCount'
      }
   }
 ]).toArray(function(err, result) {
    if (err) throw err;
    console.log(result);
    res.json(result);
    db.close();
  });
});
  });
  app.get('/gotoGuestResult', function(req, res){
    res.redirect('http://localhost:8001/ResultGuest.html');
  })
 app.get('/setting', function(req, res){
   res.redirect(`http://localhost:8001/setting.html?access_token=${req.query.accessToken}`);
 })
 app.get('/history', function(req, res){
   res.redirect(`http://localhost:8001/history.html?access_token=${req.query.accessToken}`);
 })
 app.get('/result', function(req, res){
   res.redirect(`http://localhost:8001/Result.html?access_token=${req.query.accessToken}`);
 })
 app.get('/welcome', function(req, res){
   res.redirect(`http://localhost:8001/welcome.html?access_token=${req.query.accessToken}`);
 })
 app.get('/historyRepo', function(req, res){
   res.redirect(`http://localhost:8001/historyRepo.html?repository=${req.query.repository}&access_token=${req.query.accessToken}`);
 })
 app.get('/gotoResultById', function(req, res){
   res.redirect(`http://localhost:8001/ResultById.html?id=${req.query.id}&access_token=${req.query.accessToken}`);
 })
 app.post('/getHistoryRepo', function(req, res){
   MongoClient.connect(dburl, {
   useUnifiedTopology: true,
   useNewUrlParser: true,
   }, function(err, db) {
     if (err) throw err;
     var dbo = db.db("MerryDB");
     //Sort the result by name:
   var quary = {owner: req.body.owner, repoName: req.body.reposName};
     var sort = { ExecutionID: 1 };
     dbo.collection("analysedRepoInfo").find(quary).sort(sort).toArray(function(err, result) {
       if (err) throw err;
         res.json(result);
       console.log("Query success!");
       db.close();
     });
   });
 })
app.post('/getHistory', function(req, res){
  MongoClient.connect(dburl, {
  useUnifiedTopology: true,
  useNewUrlParser: true,
  }, function(err, db) {
    if (err) throw err;
    var dbo = db.db("MerryDB");
    //Sort the result by name:
  var quary = {owner: req.body.owner};
    var sort = { date: -1 };
    dbo.collection("analysedRepoInfo").find(quary).sort(sort).toArray(function(err, result) {
      if (err) throw err;
        res.json(result);
      console.log("Query success!");
      db.close();
    });
  });
})

app.post('/saveSetting', function(req, res){
let data = JSON.stringify(req.body);
fs.writeFileSync('setting-config.json', data)
console.log("config saved!");
  res.end()
})
//localhost 8001
app.listen(global.gConfig.node_port,() => {
  console.log(`${global.gConfig.app_name} listening on port ${global.gConfig.node_port}`);
});
