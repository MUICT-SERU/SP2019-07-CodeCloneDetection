# git-clone

Clone a git repository via shell command.

## Installation

Install:

	$ npm install git-clone-sync

Require:

	var clone = require('git-clone-sync');

## API

#### `clone(repo, targetPath, [options], cb)`

Clone `repo` to `targetPath`, calling `cb` on completion.

Supported `options`:

  * `git`: path to `git` binary; default: `git` (optional).
  * `shallow`: when `true`, clone with depth 1 (optional).
  * `checkout`: revision/branch/tag to check out (optional).
  * `sync`: whether sync; default:`true` (optional).

## Copyright &amp; License

fork from: https://github.com/jaz303/git-clone.git and add sync flag

Released under the ISC license.

