class AppliedMaterial < ActiveRecord::Base
  belongs_to :material
  belongs_to :order
end
