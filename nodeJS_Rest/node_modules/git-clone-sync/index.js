var spawn = require('child_process').spawn;
var spawnSync = require('child_process').spawnSync;
var fs = require('fs');
var shelljs = require('shelljs');
module.exports = function(repo, targetPath, opts, cb) {

    if (typeof opts === 'function') {
        cb = opts;
        opts = null;
    }

    opts = opts || {};

    var git = opts.git || 'git';
    var args = ['clone'];
    var sync = (opts.sync == undefined ? true : opts.sync);

    if (opts.shallow) {
        args.push('--depth');
        args.push('1');
    }

    args.push('--');
    args.push(repo);
    args.push(targetPath);

    if (fs.existsSync(targetPath)) {
        shelljs.rm('-rf', targetPath);
    }
    var logout = function(out) {
        if (out) {
            console.log(out.stdout ? out.stdout.toString() : '');
            console.log(out.stderr ? out.stderr.toString() : '');
        }

    };
    if (sync) {
        var outp = spawnSync(git, args, { encoding: 'utf8' });
        logout(outp);
        if (opts.checkout) {
            console.log('checkout');
            _checkoutSync();
        }
        console.log('finish');
    } else {
        var process = spawn(git, args);
        process.on('close', function(status) {
            if (status == 0) {
                if (opts.checkout) {
                    _checkout();
                } else {
                    cb && cb();
                }
            } else {
                cb && cb(new Error('\"git clone\" failed with status ' + status));
            }
        });
    }

    function _checkoutSync() {
        var args = ['checkout', opts.checkout];
        var outp = spawnSync(git, args, { cwd: targetPath, encoding: 'utf8' });
        logout(outp);
    }

    function _checkout() {
        var args = ['checkout', opts.checkout];
        var process = spawn(git, args, { cwd: targetPath });
        process.on('close', function(status) {
            if (status == 0) {
                cb && cb();
            } else {
                cb && cb(new Error('\"git checkout\" failed with status ' + status));
            }
        });
    }

};