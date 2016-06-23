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
        var mphu;
        var phu;
        var n;
        var pid;
        var obj={};
        var obj2={};

		function calculateWaterNeed(a, b, c){
		        if (a < b){
		            c = b - a;
		            return c;  
		        }
		        else {
		        	c = 0;
		        	console.log('Keine Bewaesserung noetig.')
		            return c;
		        }
		}

        db.get('uu:'+req.params.uid+':measuredPlant:'+req.params.id, function(err, rep){
			if(rep) {
                //console.log(JSON.parse(rep));
                obj = JSON.parse(rep);
                mphu = obj.bodenfeuchtigkeit;
                pid = obj.plantid;

			db.get('plant:'+ pid, function(err, rep) {
				if (rep) {
    	            obj2 = JSON.parse(rep);
    	            phu = obj2.bodenfeuchtigkeit;
				}
				else {
					res.status(404).type("text").send("Die Pflanze mit der ID " + pid + " wurde nicht gefunden");
				}

				n = calculateWaterNeed(mphu, phu, n)
				console.log(n);
				var Stringresponse = {'wasserbedarf': n, 'plantid': pid, 'measuredPlantid': req.params.id};
				var JSONresponse = JSON.stringify(Stringresponse);
				console.log(JSONresponse);

        		res.type("json").send(JSONresponse);

			});


			}
			else {
				res.status(404).type('text').send('Die zugewiesene Pflanze mit der ID '+req.params.id+' wurde nicht gefunden');
			}
		});
});
 
        
//	Server Port
app.listen(8888);