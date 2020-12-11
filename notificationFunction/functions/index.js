const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendNotification = functions.database.ref('/notification/{user_id}/{notification_id}').onWrite((change, context) => {

console.log('Notification send to: ', context.params.user_id);

if(!event.data.val()){
    return console.log('A notification was deleted: ',notification_id);
}

const payload = {
    notification:{
        title:"Friend Request",
        body:"You've receieved a new Friend Request",
        icon:"default"
    }
};
    return admin.messaging().sendToDevice(,payload).then(response=>{
        console.log("This was the notification Feature");
    });
});