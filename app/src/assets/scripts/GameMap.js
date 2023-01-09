import {BaseGameObject} from "./BaseGameObject";
import { Snake } from "./Sanke";
import {Wall} from "./Wall";

export class GameMap extends BaseGameObject {
    constructor(ctx, parent, store) {
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.store = store;

        this.L = 0;

        // Centrosymmetric
        this.rows = 13;
        this.cols = 14;

        this.inner_walls_cnt = 20;

        this.walls = [];

        this.snakes = [
            new Snake({
                id : 0,
                color : "#4876EC",
                r : this.rows-2,
                c : 1,
            }, this),
            new Snake({
                id : 1,
                color : "#F94848",
                r : 1,
                c : this.cols-2,
            }, this),
        ]

    }

    create_walls(){
        const g = this.store.state.pk.gamemap;
        for (let r = 0; r < this.rows; r++){
            for(let c = 0; c < this.cols; c++){
                if(g[r][c]){
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
    }

    add_listening_events(){
        if(this.store.state.record.is_record){
            let k = 0;
            const [snake0, snake1] = this.snakes;
            const a_steps = this.store.state.record.a_steps;
            const b_steps = this.store.state.record.b_steps;
            const loser = this.store.state.record.record_loser;
            const interval_id = setInterval(() => {
                if(k >= a_steps.length - 1){
                    if(loser === "all" || loser === "A"){
                        snake0.status = "die";
                    }
                    if(loser === "all" || loser === "B"){
                        snake1.status = "die";
                    }
                    clearInterval(interval_id);
                } else{
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));
                }
                k++;
            }, 300);
        }else{
            this.ctx.canvas.focus();

            this.ctx.canvas.addEventListener("keydown", e => {
                let d = -1;
                if (e.key === 'w') d = 0;
                else if (e.key === 'd') d = 1
                else if (e.key === 's') d = 2;
                else if (e.key === 'a') d = 3;
    
                // console.log(d); 
    
                if(d >= 0){
                    this.store.state.pk.socket.send(JSON.stringify({
                        event : "move",
                        direction : d,
                    }));
                }
            });
        }
    }

    start() {
        this.create_walls();
        this.add_listening_events();
    }

    update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    check_ready() {
        //check if both snakes are ready for next round
        for(const snake of this.snakes){
            if(snake.status !== "idle") return false;
            if(snake.direction === -1) return false;
        }
        
        return true;
    }

    next_step() {
        for(const snake of this.snakes){
            snake.next_step();
        }
    }

    check_valid(cell){
        // invalid : hit the wall, hit the body of the snakes
        for(const wall of this.walls){
            if(wall.r === cell.r && wall.c === cell.c) return false;
        }

        for(const snake of this.snakes){
            let k = snake.cells.length;
            if(!snake.check_tail_increasing()){
                k --;
            }
            for(let i = 0; i < k; i++){
                if(snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) return false;
            }
        }

        return true;
    }

    update() {
        this.update_size();

        if(this.check_ready()){
            this.next_step();
        }

        this.render();
    }

    render() {
        const color_even = "#AAD751", color_odd = "#A2D149";
        for (let r = 0; r < this.rows; r++){
            for(let c = 0; c < this.cols; c++){
                if((r+c) % 2){
                    this.ctx.fillStyle = color_odd;
                }else{
                    this.ctx.fillStyle = color_even;
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
        }
    }
}