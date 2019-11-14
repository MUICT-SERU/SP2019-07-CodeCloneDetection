const {exec} = require('child_process')

exec('git clone https://github.com/weekitaus/test ./test', (err, stdout, stderr) => {
    console.log(`stdout: ${stdout}`);
    console.error(`stderr: ${stderr}`);
  });
