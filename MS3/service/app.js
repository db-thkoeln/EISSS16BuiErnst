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

    	//Calculate Method
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

		//Get information from measuredPlant
        db.get('uu:'+req.params.uid+':measuredPlant:'+req.params.id, function(err, rep){
			if(rep) {
                //console.log(JSON.parse(rep));
                obj = JSON.parse(rep);
                mphu = obj.bodenfeuchtigkeit;
                pid = obj.plantid;

        	//Get information from Plant
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

				//Sending response
        		res.type("json").send(JSONresponse);

			});


			}
			else {
				res.status(404).type('text').send('Die zugewiesene Pflanze mit der ID '+req.params.id+' wurde nicht gefunden');
			}
		});
});
 
        
//Calculate amount of needed fertilize (Server-Side)
app.get('/fertilizeneed/:uid/:id', function(req,res){
        var mFertilize;
        var fertilize;
        var mKalium;
        var mStickstoff;
        var mPhosphat;
        var pKalium;
        var pStickstoff;
        var pPhosphat;
        var kalium;
        var stickstoff;
        var phosphat;
        var pid;
        var obj={};
        var obj2={};

    	//Calculate Method
		function calculateNeed(a, b, c){
		        if (a < b){
		            c = b - a;
		            return c;  
		        }
		        else {
		        	c = 0;
		        	console.log('Keine Duengung noetig.')
		            return c;
		        }
		}
		//Get information from measuredPlant
        db.get('uu:'+req.params.uid+':measuredPlant:'+req.params.id, function(err, rep){
			if(rep) {
                //console.log(JSON.parse(rep));
                obj = JSON.parse(rep);
                mFertilize = obj.duengung;
                mKalium = mFertilize.kalium;
                mStickstoff = mFertilize.stickstoff;
                mPhosphat = mFertilize.phosphat;
                pid = obj.plantid;

        	//Get information from Plant
			db.get('plant:'+ pid, function(err, rep) {
				if (rep) {
    	            obj2 = JSON.parse(rep);
    	            fertilize = obj2.duengung;
    	            pKalium = fertilize.kalium;
                	pStickstoff = fertilize.stickstoff;
                	pPhosphat = fertilize.phosphat;
				}
				else {
					res.status(404).type("text").send("Die Pflanze mit der ID " + pid + " wurde nicht gefunden");
				}

				kalium = calculateNeed(mKalium, pKalium, kalium);
				stickstoff = calculateNeed(mStickstoff, pStickstoff, stickstoff);
				phosphat = calculateNeed(mPhosphat, pPhosphat, phosphat);

				var Stringresponse = {'kaliumbedarf': kalium, 'stickstoffbedarf': stickstoff, 'phosphatbedarf': phosphat};
				var JSONresponse = JSON.stringify(Stringresponse);

				//Sending response
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