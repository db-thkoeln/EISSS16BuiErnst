function getAllPlants(app, db) {
	return function(req, res){
		db.keys("plant:*", function(err, rep) {
			var plantList = [];
			if(rep.length == 0) {
				res.json(plantList);
				return;
			}
			console.log(rep);
			db.mget(rep, function(err, rep) {
				rep.forEach(function(val){
					plantList.push(JSON.parse(val));
				});
				plantList = plantList.map(function(plant) {
					return {
						id: plant.id,
						name: plant.name,
						licht: plant.licht,
						temperatur: plant.temperatur,
						feuchtigkeit: plant.wasser,
						duengung: { kalium: plant.duengung.kalium,
									stickstoff: plant.duengung.stickstoff,
									phosphat: plant.duengung.phosphat},
						bodenwert: plant.bodenwert,
						wachstumsphase: plant.wachstumsphase	
					};
				});

				var currentReply = plantList;
				var reply = {
    			plant: currentReply
				};



				res.json(reply);
			});
		});
	}
}
module.exports = getAllPlants;