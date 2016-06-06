function getAllStation(app, db) {
	return function(req, res){
		db.keys("station:*", function(err, rep) {
			var stationList = [];
			if(rep.length == 0) {
				res.json(stationList);
				return;
			}
			console.log(rep);
			db.mget(rep, function(err, rep) {
				rep.forEach(function(val){
					stationList.push(JSON.parse(val));
				});
				stationList = stationList.map(function(station) {
					return {
						id: station.id,
						features: station.features
					};
				});

				var currentReply = stationList;
				var reply = {
    			station: currentReply
				};



				res.json(reply);
			});
		});
	}
}
module.exports = getAllStation;