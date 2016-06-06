function getAllDuenger(app, db) {
	return function(req, res){
		db.keys("duenger:*", function(err, rep) {
			var duengerList = [];
			if(rep.length == 0) {
				res.json(duengerList);
				return;
			}
			console.log(rep);
			db.mget(rep, function(err, rep) {
				rep.forEach(function(val){
					duengerList.push(JSON.parse(val));
				});
				duengerList = duengerList.map(function(duenger) {
					return {
						id: duenger.id,
						duengung: { kalium: duenger.duengung.kalium,
									stickstoff: duenger.duengung.stickstoff,
									phosphat: duenger.duengung.phosphat}
					};
				});

				var currentReply = duengerList;
				var reply = {
    			duenger: currentReply
				};



				res.json(reply);
			});
		});
	}
}
module.exports = getAllDuenger;