const express = require('express')
// Import the axios library, to make HTTP requests
const axios = require('axios')

<<<<<<< Updated upstream
=======
const { exec, execFile } = require('child_process');
>>>>>>> Stashed changes
// const exec = require('child_process')

const config = require('./config/config.js');

const app = express()

<<<<<<< Updated upstream
const clientID = '31ec50555fca84725afa'
const clientSecret = 'a3d38c40372c265b0942450d0ebf1c1d1dad54b1'
=======
app.use(bodyParser.json({ type: 'application/json' }))

const clientID = `${global.gConfig.clientID}`;
const clientSecret = `${global.gConfig.clientSecret}`;

>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
// app.use(bodyParser.urlencoded({ extended: true }));

// app.post('/urlcloning',(req,res) => {
//   console.log(`${req.body.GitHubURL}`);
//   exec('git clone '+`${req.body.GitHubURL}`+' ./test',(error, stdout, stderr) => {
//     if (error) {
//     console.error(`exec error: ${error}`);
//     return;
//   }
//   console.log(`stdout: ${stdout}`);
//   console.error(`stderr: ${stderr}`);
//   res.send(alert(`stderr: ${stderr}`));
//   })
// })
=======
app.post('/api/clone', async function(req, res) {
  // console.log(req.params.url)
  console.log(req.body.github)

  res.json(req.body)

  await exec('git clone '+`${req.body.github}`+' ./temp', (error, stdout, stderr) => {
    if (error) {
        console.error(`exec error: ${error}`);
        return;
      }
      console.log(`stdout: ${stdout}`);
      console.error(`stderr: ${stderr}`);

    })
>>>>>>> Stashed changes

    await exec('java -jar simian-2.5.10.jar -reportDuplicateText ./temp/*.java',(error, stdout, stderr) => {
      if (error) {
          console.error(`exec error: ${error}`);
          return;
        }
        console.log(`stdout: ${stdout}`);
        console.error(`stderr: ${stderr}`);
})
})
//localhost 8001
app.listen(global.gConfig.node_port,() => {
  console.log(`${global.gConfig.app_name} listening on port ${global.gConfig.node_port}`);
});
