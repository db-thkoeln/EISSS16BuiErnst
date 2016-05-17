function getAllUsers(app, db) {
	return function(req, res){
		db.keys("user:*", function(err, rep) {
			var userList = [];
			if(rep.length == 0) {
				res.json(userList);
				return;
			}
			console.log(rep);
			db.mget(rep, function(err, rep) {
				rep.forEach(function(val){
					userList.push(JSON.parse(val));
				});
				userList = userList.map(function(user) {
					return {
						id: user.id,
						name: user.name,
						passwort: user.passwort,
					};
				});

				var currentReply = userList;
				var reply = {
    			user: currentReply
				};



				res.json(reply);
			});
		});
	}
}
module.exports = getAllUsers;