let util = require('util')
let exec = require('child_process').exec
let exec_prom = util.promisify(exec)

exec_prom('ls').then(()=>{console.log('done')})


async function doNext(){
  await exec_prom('ls');
  // do something after
  console.log("This is my ip");
}
