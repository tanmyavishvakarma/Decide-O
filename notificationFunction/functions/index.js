const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendNotification = functions.database.ref('/notification/{user_id}/{notification_id}').onWrite((change, context) => {

console.log('Testing stuff', context.params.user_id);
return 0;
});