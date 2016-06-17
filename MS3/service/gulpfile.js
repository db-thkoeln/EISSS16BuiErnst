var gulp = require("gulp"); 
var eslint = require("gulp-eslint"); 
var jsFiles = "**/*.js"; 
var redis = require("redis"); 
var bodyParse = require("body-parser"); 
var jsonParser = bodyParse.json(); 
// Create our Server 
var db = redis.createClient(); 


gulp.task("lint", function() { 
	return gulp.src(jsFiles) 
		.pipe(eslint("lint.json")) 
		.pipe(eslint.format()) 
		.pipe(eslint.failAfterError()); 
}); 
gulp.task("default", function() { 
	gulp.start(["lint"]); 
	gulp.watch(jsFiles, ["lint"]); 
}); 

gulp.task("db-flush", function() { 
	db.flushdb( function (err, didSucceed) { 
        console.log(didSucceed); // true 
    }); 


}); 

gulp.task("db-dummy", function() { 
	// Plant Dummies
	var newPlant1 = { 
	"id": "1",	
	"name": "Aloe Vera", 
 	"lichtstaerke": 20000, 
 	"temperatur": 20, 
 	"bodenfeuchtigkeit": 2, 
 	"duengung": { 
 				"kalium": 11, 
 				"stickstoff": 15, 
 				"phosphat": 22 
 				}, 
 	"bodenwert": 7, 
 	"wachstumsphase" : "Sommer"
	} 
	var newPlant2 = { 
	"id": "2",	
	"name": "Mimosa pudica", 
 	"lichtstaerke": 40000, 
 	"temperatur": 15, 
 	"bodenfeuchtigkeit": 0.2, 
 	"duengung": { 
 				"kalium": 20, 
 				"stickstoff": 20, 
 				"phosphat": 20 
 				}, 
 	"bodenwert": 7.5, 
 	"wachstumsphase" : "Sommer"
	}
   	db.set("plant:1", JSON.stringify(newPlant1)); 
	db.set("plant:2", JSON.stringify(newPlant2)); 
	
	
	// User Dummies 
	var newUser1 = { 
	"id": "1", 
	"name": "Markus", 
 	"passwort": "Ernst" 
	} 
	var newUser2 = { 
	"id": "2", 
	"name": "Duc", 
 	"passwort": "Bui" 
	} 
 	db.set("user:1", JSON.stringify(newUser1)); 
 	db.set("user:2", JSON.stringify(newUser2)); 


 	// Station Dummies 
 	var newStation1 = { 
 	"id": "1", 
 	"features": "outdoor" 
 	} 
	var newStation2 = { 
 	"id": "2", 
 	"features": "outdoor" 
 	} 
	var newStation3 = { 
 	"id": "3", 
 	"features": "outdoor" 
 	} 
	var newStation4 = { 
 	"id": "4", 
 	"features": "indoor" 
 	} 
	var newStation5 = { 
 	"id": "5", 
 	"features": "indoor" 
 	} 
	var newStation6 = { 
 	"id": "6", 
 	"features": "indoor" 
 	} 

 	db.set('station:1', JSON.stringify(newStation1)); 
	db.set('station:2', JSON.stringify(newStation2)); 
	db.set('station:3', JSON.stringify(newStation3)); 
	db.set('station:4', JSON.stringify(newStation4)); 
	db.set('station:5', JSON.stringify(newStation5)); 
	db.set('station:6', JSON.stringify(newStation6)); 
	
	
	// Duenger Dummies 
 	var newDuenger1 = { 
 	"id": "1",
	"bezeichnung": "Vollwertduenger NPK", 
 	"duengung": { 
				"kalium": 20, 
 				"stickstoff": 20, 
 				"phosphat": 20 
 				}
 	} 
	var newDuenger2 = { 
 	"id": "2",
	"bezeichnung": "Halbwertduenger NP", 
 	"duengung": { 
				"kalium": 0, 
 				"stickstoff": 20, 
 				"phosphat": 20 
 				}
 	}
	var newDuenger3 = { 
 	"id": "3",
	"bezeichnung": "Halbwertduenger NK", 
 	"duengung": { 
				"kalium": 20, 
 				"stickstoff": 20, 
 				"phosphat": 0 
 				}
 	}
	var newDuenger4 = { 
 	"id": "4",
	"bezeichnung": "Halbwertduenger PK", 
 	"duengung": { 
				"kalium": 20, 
 				"stickstoff": 0, 
 				"phosphat": 20 
 				}
 	}
	var newDuenger5 = { 
 	"id": "5",
	"bezeichnung": "Vollwertduenger NPK", 
 	"duengung": { 
				"kalium": 10, 
 				"stickstoff": 10, 
 				"phosphat": 10 
 				}
 	}
	
 	db.set('duenger:1', JSON.stringify(newDuenger1)); 
	db.set('duenger:2', JSON.stringify(newDuenger2)); 
	db.set('duenger:3', JSON.stringify(newDuenger3)); 
	db.set('duenger:4', JSON.stringify(newDuenger4)); 
	db.set('duenger:5', JSON.stringify(newDuenger5)); 
	
	
	// measuredPlant Dummies
	var newMeasuredPlant1 = { 
	"id": "1",	
	"plantid": "1", 
 	"lichtstaerke": 90000, 
 	"temperatur": 30, 
 	"bodenfeuchtigkeit": 0.2, 
 	"duengung": { 
 				"kalium": 5, 
 				"stickstoff": 8, 
 				"phosphat": 7 
 				}, 
 	"bodenwert": 7, 
 	"stationid" : "2"
	} 
	var newMeasuredPlant2 = { 
	"id": "2",	
	"plantid": "2", 
 	"lichtstaerke": 90000, 
 	"temperatur": 30, 
 	"bodenfeuchtigkeit": 0.2, 
 	"duengung": { 
 				"kalium": 20, 
 				"stickstoff": 20, 
 				"phosphat": 20 
 				}, 
 	"bodenwert": 7.5, 
 	"stationid" : "1"
	}
	var newMeasuredPlant3 = { 
	"id": "3",	
	"plantid": "1", 
 	"lichtstaerke": 90000, 
 	"temperatur": 30, 
 	"bodenfeuchtigkeit": 0.2, 
 	"duengung": { 
 				"kalium": 5, 
 				"stickstoff": 8, 
 				"phosphat": 7 
 				}, 
 	"bodenwert": 9, 
 	"stationid" : "3"
	} 
	var newMeasuredPlant4 = { 
	"id": "4",	
	"plantid": "2", 
 	"lichtstaerke": 90000, 
 	"temperatur": 30, 
 	"bodenfeuchtigkeit": 0.2, 
 	"duengung": { 
 				"kalium": 20, 
 				"stickstoff": 20, 
 				"phosphat": 20 
 				}, 
 	"bodenwert": 9, 
 	"stationid" : "4"
	}
	
   	db.set("uu:1:measuredPlant:1", JSON.stringify(newMeasuredPlant1)); 
	db.set("uu:1:measuredPlant:2", JSON.stringify(newMeasuredPlant2)); 
	db.set("uu:2:measuredPlant:3", JSON.stringify(newMeasuredPlant3)); 
	db.set("uu:2:measuredPlant:4", JSON.stringify(newMeasuredPlant4)); 

 	
 }); 
