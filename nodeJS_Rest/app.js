const express = require('express')
// Import the axios library, to make HTTP requests
const axios = require('axios')

var bodyParser = require('body-parser')

const { exec, execFile } = require('child_process');

// const exec = require('child_process')

const config = require('./config/config.js');

const app = express()

app.use(bodyParser.json({ type: 'application/json' }))

const clientID = `${global.gConfig.clientID}`;
const clientSecret = `${global.gConfig.clientSecret}`;

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
  // console.log(req.params.url)
  console.log(req.body.github)

  res.json(req.body)

  exec('git clone '+`${req.body.github}`+' ./temp', (error, stdout, stderr) => {
    if (error) {
        console.error(`exec error: ${error}`);
        return;
      }
      console.log(`stdout: ${stdout}`);
      console.error(`stderr: ${stderr}`);

    })

})
// , exec('java -jar simian-2.5.10.jar -reportDuplicateText ./temp/*.java',(error, stdout, stderr) => {
//   if (error) {
//       console.error(`exec error: ${error}`);
//       return;
//     }
//     console.log(`stdout: ${stdout}`);
//     console.error(`stderr: ${stderr}`);
// })
//localhost 8001
app.listen(global.gConfig.node_port,() => {
  console.log(`${global.gConfig.app_name} listening on port ${global.gConfig.node_port}`);
});
