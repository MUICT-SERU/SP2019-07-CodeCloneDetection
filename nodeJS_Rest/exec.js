var MongoClient = require('mongodb').MongoClient;
var url = "mongodb://localhost:27017/";

MongoClient.connect(url, {
useUnifiedTopology: true,
useNewUrlParser: true,
}, function(err, db) {
  if (err) throw err;
  var dbo = db.db("Data");
  var myobj = { name: "Company Inc", address: "Highway 37" };
    dbo.collection("analysedClone").insertOne(myobj, function(err, res) {
      if (err) throw err;
      console.log("document inserted");
      db.close();
});
});
