const BASE_GAME_OBJECTS = [];


export class BaseGameObject {
    constructor() {
        BASE_GAME_OBJECTS.push(this);
        this.timedelta = 0;
        this.has_called_start = false;
    }

    start() {
        //only 1st time
    }

    update() {
        //every frame except the 1st
    }

    on_destroy() {
        //before destroy
    }

    destroy() {

        this.on_destroy();


        for (let i in BASE_GAME_OBJECTS) {
            const obj = BASE_GAME_OBJECTS[i];
            if (obj == this) {
                BASE_GAME_OBJECTS.splice(i);
                break;
            }
        }
    }
}

let last_timestamp;
const step = timestamp => {

    for (let obj of BASE_GAME_OBJECTS){
        if (!obj.has_called_start){
            obj.has_called_start = true;
            obj.start();
        }else{
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    }

    last_timestamp = timestamp;

    requestAnimationFrame(step)
}

requestAnimationFrame(step)