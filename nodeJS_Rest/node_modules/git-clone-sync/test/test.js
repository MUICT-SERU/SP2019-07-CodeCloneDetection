var gitClone = require('../index');

gitClone('https://github.com/yale8848/git-clone.git', './test-checkout', {
        checkout: 'a76362b0705d4126fa4462916cabb2506ecfe8e2',
        sync: true
    },
    function(err) {
        console.log('complete!');
        console.log(err);
    });