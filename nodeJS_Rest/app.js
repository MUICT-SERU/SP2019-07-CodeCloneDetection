const express = require('express')
// Import the axios library, to make HTTP requests
const axios = require('axios')

var bodyParser = require('body-parser')

const exec = require('child_process').exec;
const execSync = require('child_process').execSync;

const config = require('./config/config.js')
const fs = require('fs');
const app = express()

app.use(bodyParser.json({ type: 'application/json' }))

const clientID = `${global.gConfig.clientID}`;
const clientSecret = `${global.gConfig.clientSecret}`;

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
    const accessToken = response.data.access_token
    //const username = response.data.username
    // redirect the user to the welcome page, along with the access token
    res.redirect(`/welcome.html?access_token=${accessToken}`)
  })
})

app.use(express.static(__dirname + '/frontend'))

app.post('/api/clone', function(req, res) {
  res.json(req.body)

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
        var user = `${req.body.user}`;
        let data = fs.readFileSync('./output.txt','utf8');

        let obj = {owner: user, result: data};
        console.log(obj);

})
// app.delete('/api/logout', function(req, res){
//   res.redirect('/');
// });

app.post('/api/readFile',function(req,res, next){
//   // lineReader.eachLine('./output.txt', function(line ,last) {
//   //         let obj = {};
//   //         for (let i = 0; i<line.length; i++) {
//   //             obj = { [i] : line};
//   //         }
//   //         console.log(obj);
  res.json(req.body)
var userName = `${req.body.user}`;
    fs.readFile('./output.txt','utf8', (err, data)=>{
       if (err) throw err;
      let obj = { owner: userName, result : data};
      console.log(userName);
      res.json(obj);
    });
})
//localhost 8001
app.listen(global.gConfig.node_port,() => {
  console.log(`${global.gConfig.app_name} listening on port ${global.gConfig.node_port}`);
});
