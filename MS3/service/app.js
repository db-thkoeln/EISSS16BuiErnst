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

// Calculate amount of needed water (pre-Weather-API) 
app.get('/waterneed/:uid/:id', function(req,res){
        var mphu=0;
        var phu=0;
        var n=0;
        var pid=1;
        var obj={};
        var obj2={};
        db.get('uu:'+req.params.uid+':measuredPlant:'+req.params.id, function(err, rep){
			if(rep) {
				//res.type('json').send(rep);
                //console.log(JSON.parse(rep));
                obj = JSON.parse(rep);
                mphu = obj.bodenfeuchtigkeit;
                //pid = parseInt(obj.plantid); //dynamic does not work yet
                console.log(mphu);
			}
			else {
				res.status(404).type('text').send('Die zugewiesene Pflanze mit der ID '+req.params.id+' wurde nicht gefunden');
			}
		});
        
        db.get('plant:'+ pid, function(err, rep) {
			if (rep) {
				//res.type("json").send(rep);
                obj2 = JSON.parse(rep);
                phu = obj2.bodenfeuchtigkeit;
                console.log(phu);
			}
			else {
				res.status(404).type("text").send("Die Pflanze mit der ID " + pid + " wurde nicht gefunden");
			}
		});
        
        function calculateWaterNeed(a, b, c){
        if (a < b){
            c = b - a;
            return c;  
        }
        else {
            return console.log('Keine Bewaesserung noetig.');
        }
        }
        n = calculateWaterNeed(mphu, phu, n)
        res.send(n);
});
 
        
//	Server Port
app.listen(8888);