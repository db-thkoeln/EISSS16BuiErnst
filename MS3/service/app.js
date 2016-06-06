//	required Modules
var express = require("express");
var bodyParse = require("body-parser");
var jsonParser = bodyParse.json();
var redis = require("redis");
//	Create our Server
var app = express();
var db = redis.createClient();
//	Use bodyParser
app.use(jsonParser);
//	//////////////////////////////////////////
/* Register Planto API */
require("./api/plant/import").register(app, db);
require("./api/station/import").register(app, db);
require("./api/user/import").register(app, db);
require("./api/duenger/import").register(app, db);
//	Server Port
app.listen(8888);