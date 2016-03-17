(function($){

    var BUTTON = '<button value=":value" class=":class">:text</button>';
    var DOTS = '<span class=":class">..</span>';

    var VALUE = ':value';
    var CLASS = ':class';
    var TEXT = ':text';

    var RETREAT = 3;

    var CSS_CLASS_ALL = 'pagination-button-all';
    var CSS_CLASS_ALL_DOT = '.'+CSS_CLASS_ALL;

    var CSS_CLASS_BUTTON = 'pagination-button';
    var CSS_CLASS_BUTTON_DOT = '.'+CSS_CLASS_BUTTON;

    var CSS_CLASS_BUTTON_SELECTED = 'pagination-button-selected';
    var CSS_CLASS_BUTTON_SELECTED_DOT = '.'+CSS_CLASS_BUTTON_SELECTED;

    var CSS_CLASS_DOTS = 'dots';
    var CSS_CLASS_DOTS_DOT = '.'+CSS_CLASS_DOTS;

    var Pagination = function(options){
        options = options || {};

        var _self = this;

        _self._buttons_holder = options.holder || $('body');

        var _buttons_options = options.buttons || {};

        _self._buttons_text = _buttons_options.text || function(index){ return index+1; };
        _self._buttons_css_style = _buttons_options.styleClass || CSS_CLASS_BUTTON;
        _self._buttons_css_style_selected = _buttons_options.styleClassSelected || CSS_CLASS_BUTTON_SELECTED;

        _self._count = options.countPages || 0;
        _self._current = options.currentPage || 1;
        _self._start_page = _self._current;

        if(options.more){
            options.more.click(function(e){
                ++_self._current;
                $(CSS_CLASS_ALL_DOT+'[value='+_self._current+']')
                    .removeClass(_self._buttons_css_style)
                    .addClass(_self._buttons_css_style_selected);
                $(CSS_CLASS_ALL_DOT+'[value='+(_self._current+1)+']').show();
                if(options.moreClick){
                    options.moreClick(e, _self._current, _self._current == _self._count);
                }
                _self.dots();
            });
        }
    };

    Pagination.prototype.render = function(){
        var html = [],
            _count = this._count,
            _current = this._current;
        for(var i = 0; i < _count; ++i){
            var button = BUTTON
                .replace(VALUE, (i+1).toString())
                .replace(TEXT, this._buttons_text(i));
            button = _current - 1 == i
                ? button.replace(CLASS, this._buttons_css_style_selected + ' ' + CSS_CLASS_ALL)
                : button.replace(CLASS, this._buttons_css_style + ' ' + CSS_CLASS_ALL);

            html.push(button);
        }
        this._buttons_holder.html(html.join(''));

        if(_count >= 10){
            var _buttons = this._buttons_holder.find(CSS_CLASS_ALL_DOT);
            _buttons.each(function(index, obj){
                if((index >= RETREAT && index < _current - 2) ||
                    (index > _current  && index < _buttons.length - RETREAT)){
                    $(obj).hide();
                }
            });
            this.dots();
        }
    };

    Pagination.prototype.dots = function(){
        var _start = this._start_page,
            _current = this._current,
            _holder = this._buttons_holder,
            _buttons = _holder.find(CSS_CLASS_ALL_DOT);

        _holder.find(CSS_CLASS_DOTS_DOT).remove();
        if(_start > RETREAT + 2)
            $(DOTS.replace(CLASS, CSS_CLASS_DOTS)).insertBefore(CSS_CLASS_ALL_DOT+'[value='+(_start-2)+']');
        if(_current < _buttons.length - 4)
            $(DOTS.replace(CLASS, CSS_CLASS_DOTS)).insertAfter(CSS_CLASS_ALL_DOT+'[value='+(_current+1)+']');
    };

    function error(message){
        throw Error(message);
    }

    $.fn.pagination = function(options){
        options = options || {};
        options.holder = this;
        this._pagination = new Pagination(options);
        this._pagination.render();
        return this;
    };

}(jQuery));