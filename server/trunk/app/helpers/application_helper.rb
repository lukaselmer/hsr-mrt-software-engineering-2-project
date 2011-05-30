module ApplicationHelper
  def form_error_messages f
    f.error_messages :object_name => f.object.class.model_name.human
  end
  
  def fl; '<div class="fl">' + yield + '</div>'; end
  def fr; '<div class="fr">' + yield + '</div>'; end
  def clearer; '<div class="clearer"></div>'; end
end
