export class Cell {
    constructor(r, c){
        this.r = r;
        this.c = c;

        // center
        this.x = c+0.5;
        this.y = r+0.5;
    }
}