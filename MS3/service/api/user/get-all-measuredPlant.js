function getAllMeasuredPlant(app, db) {
	return function(req, res) {
		db.keys("uu:" + req.params.uid + ":measuredPlant:*", function(err, rep) {
			var plantList = [];
			if (rep.length === 0) {
				res.json(plantList);
				return;
			}
			db.mget(rep, function(err, rep) {
				rep.forEach(function(val) {
					plantList.push(JSON.parse(val));
				});
				var replies = [];
				plantList.forEach(function(plant) {
					db.get("plant:" + plant.plantid, function(plantError, plantReply) {
						if (plantReply) {
							replies.push(JSON.parse(plantReply));
						}
						if (replies.length === plantList.length) {
							res.type("json").send({
								replies: replies,
								measuredPlant: plantList
							});
						}
					});
				});
			});
		});
	}
}
module.exports = getAllMeasuredPlant;