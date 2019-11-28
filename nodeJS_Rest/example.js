const mongoose = require('mongoose');
// const passport = require('passport');
// const _ = require('lodash');



// const mongoose = require('mongoose');


var userSchema = new mongoose.Schema({
    username:{
        type: String
    },
    Score:{
        type: Number
    }
});
mongoose.model('User',userSchema)


const User = mongoose.model('User');
var user = new User();
user.username = "kao";
user.Score = 123;
user.save((err,doc)=>{
    if(!err){
        console.log(doc);
    }
    else{
        console.log(err);
    }
})