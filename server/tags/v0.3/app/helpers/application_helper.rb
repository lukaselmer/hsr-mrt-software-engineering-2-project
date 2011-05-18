module ApplicationHelper
  def form_error_messages f
    f.error_messages :object_name => f.object.class.model_name.human
  end
end
