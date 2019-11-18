const {exec} = require('child_process')

exec('git clone https://github.com/weekitaus/test.git ./test', (error, stdout, stderr) => {
  if (error) {
      console.error(`exec error: ${error}`);
      return;
    }
    console.log(`stdout: ${stdout}`);
    console.error(`stderr: ${stderr}`);
  });
