const functions = require('firebase-functions');
const admin = require('firebase-admin');
const { user } = require('firebase-functions/lib/providers/auth');
admin.initializeApp();

exports.sendNotification = functions.database.ref('/notification/{user_id}/{notification_id}').onWrite((change, context) => {

console.log('Notification send to: ', context.params.user_id);

if(!event.data.val()){
    return console.log('A notification was deleted: ',notification_id);
}

const deviceToken =admin.database().ref(`/Users/${user_id}/device_token`).once('value');

return deviceToken.then(result=>{

    const token_id=result.val();

    const payload = {
    notification:{
        title:"Friend Request",
        body:"You've receieved a new Friend Request",
        icon:"default"
    }
};
    return admin.messaging().sendToDevice(token_id,payload).then(response=>{
        return console.log("This was the notification Feature");
    });
   
});


});