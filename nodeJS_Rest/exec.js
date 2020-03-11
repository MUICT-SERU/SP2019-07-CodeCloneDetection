const args = [
  "clone",
  'https://github.com/weekitaus/junit4.git',
  "./temp"
];

const child = require("child_process").spawnSync("git", args);
console.log(`${child.stderr}`);
